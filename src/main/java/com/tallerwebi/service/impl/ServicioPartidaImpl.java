package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioPregunta;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioMisiones;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.service.ServicioPartida;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.tallerwebi.dominio.enums.TIPO_PARTIDA.MULTIJUGADOR;

@Service
public class ServicioPartidaImpl implements ServicioPartida {
    @Autowired
    private SessionFactory sessionFactory;
    private final RepositorioPartida repositorioPartida;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioPregunta repositorioPregunta;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Object lock = new Object();

    @Autowired
    private ServicioMisionesUsuario servicioMisionesUsuario;
    @Autowired
    private ServicioPartida servicioPartida;
    private static final ConcurrentHashMap<Long, Object> locksPorPartida = new ConcurrentHashMap<>();
    @Autowired
    private ServicioPartidaTransaccional partidaTransaccional;
    @Autowired
    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, RepositorioUsuario repositorioUsuario, RepositorioPregunta repositorioPregunta,SimpMessagingTemplate messagingTemplate) {
        this.repositorioPartida = repositorioPartida;
        this.messagingTemplate = messagingTemplate;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioPregunta = repositorioPregunta;
    }

    // --------------------------------- CREAR O UNIRSE A PARTIDA --------------------------------- //
    @Override
    @Transactional
    public Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego) {
        synchronized(lock) {
            Partida partida;

            if (repositorioPartida.jugadorEstaJugando(jugador.getId())) {
                partida = repositorioPartida.obtenerPartidaActivaDeJugador(jugador.getId());
            } else {

                jugador.setVidas(jugador.getVidas() - 1);
                repositorioUsuario.modificar(jugador);
                em.flush();

                List<Partida> partidasAbiertas = repositorioPartida.obtenerPartidasAbiertaPorModo(modoJuego);

                Partida partidaParaUnirse = null;
                for (Partida p : partidasAbiertas) {
                    List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(p.getId());

                    if (jugadores.size() < 2 && jugadores.stream().noneMatch(u -> u.getId().equals(jugador.getId()))) {
                        partidaParaUnirse = p;
                        break;
                    }
                }

                if (partidaParaUnirse != null) {
                    UsuarioPartida up = new UsuarioPartida(jugador, partidaParaUnirse);
                    repositorioPartida.agregarUsuarioPartidaRelacion(up);
                    sessionFactory.getCurrentSession().flush();

                    List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(partidaParaUnirse.getId());
                    if (jugadores.size() == 2) {
                        // Obtener la entidad gestionada por Hibernate
                        Session session = sessionFactory.getCurrentSession();
                        Partida partidaGestionada = session.get(Partida.class, partidaParaUnirse.getId());

                        partidaGestionada.setEstadoPartida(ESTADO_PARTIDA.EN_CURSO);
                        session.flush(); // Forzar actualización inmediata
                    }
                    partida = partidaParaUnirse;
                } else {
                    Partida nuevaPartida = new Partida();
                    nuevaPartida.setTipo(modoJuego);
                    nuevaPartida.setEstadoPartida(ESTADO_PARTIDA.ABIERTA);
                    if(modoJuego.equals(MULTIJUGADOR)) {
                        nuevaPartida.setTurnoActual(jugador);
                    }
                    repositorioPartida.guardarPartida(nuevaPartida);
                    sessionFactory.getCurrentSession().flush();

                    UsuarioPartida up = new UsuarioPartida(jugador, nuevaPartida);
                    repositorioPartida.agregarUsuarioPartidaRelacion(up);
                    sessionFactory.getCurrentSession().flush();

                    partida = nuevaPartida;
                }
            }

            // Aquí el envío correcto de mensajes
            Usuario rival = repositorioPartida.obtenerRivalDePartida(partida.getId(), jugador.getId());

            if (rival != null) {
                String avatarRival = repositorioUsuario.obtenerImagenAvatarSeleccionado(rival.getId());
                String avatarJugador = repositorioUsuario.obtenerImagenAvatarSeleccionado(jugador.getId());

                UsuarioDTO rivalDTO = new UsuarioDTO(rival, avatarRival);
                UsuarioDTO jugadorDTO = new UsuarioDTO(jugador, avatarJugador);


                messagingTemplate.convertAndSendToUser(
                        jugador.getNombreUsuario(),
                        "/queue/partida",
                        rivalDTO
                );


                messagingTemplate.convertAndSendToUser(
                        rival.getNombreUsuario(),
                        "/queue/partida",
                        jugadorDTO
                );
            }

            return partida;
        }
    }










    // --------------------------------- CONSULTAS, ETC --------------------------------- //

    @Override
    @Transactional
    public List<Partida> obtenerPartidasAbiertasPorModo(TIPO_PARTIDA tipo){
        return repositorioPartida.obtenerPartidasAbiertaPorModo(tipo);
    }

    @Override
    @Transactional
    public List<Usuario> obtenerJugadoresEnPartida(Long id){
        return this.repositorioPartida.obtenerJugadoresDePartida(id);
    }



    @Override
    @Transactional
    public Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario) {
        Pregunta pregunta = this.repositorioPregunta.obtenerPregunta(categoria, idUsuario);

        if (pregunta != null && pregunta.getRespuestas() != null) {
            Collections.shuffle(pregunta.getRespuestas()); // mezcla directamente el List
        }


        return pregunta;
    }


    @Override
    @Transactional
    public Respuesta buscarRespuestaPorId(Long idrespuestaSeleccionada) {
        return repositorioPregunta.buscarRespuestaPorId(idrespuestaSeleccionada);
    }
    @Override
    @Transactional(readOnly = true)
    public Partida buscarPartidaPorId(Long idPartida){
        Partida p = repositorioPartida.buscarPartidaPorId(idPartida);
//        em.refresh(p);
        return p;
    }


    @Override
    @Transactional
    public SiguientePreguntaSupervivencia obtenerPreguntaSupervivencia(Long idPartida, Usuario usuario, ResultadoRespuesta resultadoUltimo) {
        Object lock = locksPorPartida.computeIfAbsent(idPartida, k -> new Object());

        synchronized (lock) {
            Partida partida = buscarPartidaPorId(idPartida);
            em.flush();
            em.clear();

            // Orden actual para este jugador
            Integer ordenJugador = (resultadoUltimo != null && resultadoUltimo.getOrden() != null)
                    ? resultadoUltimo.getOrden() + 1
                    : 1;

            // 🔍 Ver si ya existe una pregunta generada para ese orden
            SiguientePreguntaSupervivencia existente = repositorioPartida.obtenerSiguientePreguntaEntidad(partida, ordenJugador);
            if (existente != null) {
                return existente;
            }

            //  Solo crear nueva si no existe aún
            List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(idPartida);
            Pregunta nuevaPregunta = repositorioPregunta.obtenerPreguntaSupervivencia(jugadores);

            if (nuevaPregunta == null) {
                finalizarPartida(idPartida);
                return null;
            }

            SiguientePreguntaSupervivencia siguiente = new SiguientePreguntaSupervivencia(
                    partida, jugadores.get(0), jugadores.get(1), ordenJugador, nuevaPregunta
            );

            try {
                partidaTransaccional.guardarSiguientePregunta(siguiente);
            } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                // Otro thread la creó antes
                return partidaTransaccional.obtenerPreguntaExistenteConNuevaTransaccion(partida, ordenJugador);
            }

            return siguiente;
        }
    }

    @Override
    @Transactional
    public void finalizarPartida(Long idPartida) {
        Partida p = repositorioPartida.buscarPartidaPorId(idPartida);
        p.setEstadoPartida(ESTADO_PARTIDA.FINALIZADA);
        repositorioPartida.actualizarPartida(p);
        locksPorPartida.remove(idPartida);
    }

    @PersistenceContext
    private EntityManager em;
    // Método para chequear si la partida terminó
    @Transactional
    @Override
    public boolean partidaTerminada(Long idPartida) {
        Partida p = repositorioPartida.buscarPartidaPorId(idPartida);
        em.refresh(p);
        return p.getEstadoPartida().equals(ESTADO_PARTIDA.FINALIZADA);

    }

    @Override
    public Pregunta buscarPreguntaPorId(Long idPregunta) {
        return repositorioPregunta.buscarPreguntaPorId(idPregunta);
    }


    @Override
    @Transactional
    public ResultadoRespuesta obtenerUltimoResultadoRespuestaDeJugador(Long idPartida, Usuario usuario){
        return repositorioPartida.obtenerUltimoResultadoRespuestaEnPartidaPorJugador(idPartida, usuario);
    }

    @Transactional
    @Override
    public void actualizarResultadoRespuesta(ResultadoRespuesta rr) {
        repositorioPartida.actualizarResultadoRespuesta(rr);

        if (rr.getRespuestaSeleccionada() != null || Boolean.TRUE.equals(rr.getTiempoTerminadoRespuestaNula())) {
            boolean existe = repositorioPartida.existeUsuarioRespondePregunta(rr.getUsuario().getId(), rr.getPregunta().getId());
            if (!existe) {
                repositorioPartida.guardarUsuarioRespondePregunta(new UsuarioRespondePregunta(rr.getUsuario(), rr.getPregunta()));
            }
        }

        em.flush();
    }


    @Override
    @Transactional
    public ResultadoRespuesta obtenerResultadoPorPartidaUsuarioYPregunta(Long idPartida, Usuario usuario, Pregunta preguntaResp) {
        return repositorioPartida.obtenerResultadoPorPartidaUsuarioYPregunta(idPartida, usuario, preguntaResp);

    }

    @Override
    public SiguientePreguntaSupervivencia obtenerSiguientePreguntaEntidad(Long idPartida, Integer orden) {
        return repositorioPartida.obtenerSiguientePreguntaEntidad(buscarPartidaPorId(idPartida), orden);
    }

    @Override
    public ResultadoRespuesta obtenerResultadoPorOrdenYPregunta(Long idPartida, Usuario usuario, int ordenCorrecto, Pregunta siguientePregunta) {
        return repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, usuario, ordenCorrecto, siguientePregunta);
    }

    @Override
    public List<Partida> obtenerPartidasAbiertasConTurnoEnNull(TIPO_PARTIDA modoJuego, Usuario jugador) {
        return repositorioPartida.obtenerPartidasAbiertasConTurnoEnNull(modoJuego, jugador);
    }


    @Transactional
    @Override
    public CategoriasGanadasEnPartida obtenerCategoriasGanadasDeUsuarioEnPartida(Partida partida, Usuario usuario){
        return repositorioPartida.obtenerCategoriasGanadasDeUsuarioEnPartida(partida, usuario);
    }

    @Override
    @Transactional
    public void agregarCategoriaGanadaEnPartida(Long idPartida, Usuario usuario, CATEGORIA_PREGUNTA categoriaCorona) {
        Partida partida = buscarPartidaPorId(idPartida);
        CategoriasGanadasEnPartida cat = obtenerCategoriasGanadasDeUsuarioEnPartida(partida, usuario);

        if (cat == null) {
            cat = new CategoriasGanadasEnPartida();
            cat.setPartida(partida);
            cat.setUsuario(usuario);
            cat.getCategoriasGanadas().add(categoriaCorona); // HashSet → no se repite
            repositorioPartida.guardarCategoriasGanadas(cat);
        } else {
            cat.getCategoriasGanadas().add(categoriaCorona); // si ya existe, simplemente se agrega la nueva
            repositorioPartida.actualizarCategoriasGanadas(cat);
        }

        em.flush();
    }


    @Transactional
    @Override
    public List<Partida> obtenerPartidasAbiertasOEnCursoMultijugadorDeUnJugador(Usuario u){
        return repositorioPartida.obtenerPartidasAbiertasOEnCursoMultijugadorDeUnJugador(u);
    }


    public Pregunta buscarPreguntaPorOrden(Long idPartida, int orden) {
        SiguientePreguntaSupervivencia sp = repositorioPartida.obtenerSiguientePreguntaEntidad(buscarPartidaPorId(idPartida), orden);
        if (sp != null) return sp.getSiguientePregunta();
        return null;
    }











    // --------------------------------- SUPERVIVENCIA / MULTIJUGADOR --------------------------------- //
    @Override
    @Transactional
    public ResultadoRespuesta validarRespuesta(ResultadoRespuesta resultado, TIPO_PARTIDA modoJuego, HttpServletRequest request) throws UsuarioNoExistente {

        //recibe resultadoRespuesta con la respuesta seleccionada desde el controller
        // chequea q ambos hayan respondido (resultado respuesta del rival de mismo orden y misma pregunta)
        // va a partida supervivencia, chequea respuesta del jugador con la del rival, devuelve resulado respuesta con siguiente pregunta y sin respuesta, o devuelve null.
        Partida partida = resultado.getPartida();
        Usuario jugador = resultado.getUsuario();
        Pregunta preguntaRespondida = resultado.getPregunta();
        Respuesta respuestaSeleccionada = resultado.getRespuestaSeleccionada();
        Integer orden = resultado.getOrden();

        boolean existe = repositorioPartida.existeUsuarioRespondePregunta(jugador.getId(), preguntaRespondida.getId());
        if (!existe) {
            repositorioPartida.guardarUsuarioRespondePregunta(new UsuarioRespondePregunta(jugador, preguntaRespondida));
        }
        em.flush();

        boolean ambosRespondieron = false;

        if (modoJuego == TIPO_PARTIDA.SUPERVIVENCIA) {
            ambosRespondieron = chequearAmbosRespondieron(partida.getId(), jugador, orden);

        }

        ResultadoRespuesta resultadoRespuestaSiguiente = null;
        if (ambosRespondieron) {
            if (modoJuego == TIPO_PARTIDA.SUPERVIVENCIA) {
                resultadoRespuestaSiguiente = partidaSupervivencia(resultado);
            }
        }
        if (modoJuego == TIPO_PARTIDA.MULTIJUGADOR) {
            resultadoRespuestaSiguiente = partidaMultijugador(resultado, request);
        }


        // Fallback si no avanzó la partida

        boolean terminoPartida = false;
        if (modoJuego == TIPO_PARTIDA.SUPERVIVENCIA) {
            terminoPartida = partidaTerminada(partida.getId()) ||
                    (resultadoRespuestaSiguiente == null && ambosRespondieron);

            notificarEstadoPartida(partida.getId(), jugador, ambosRespondieron, terminoPartida, modoJuego);
        } else {
            terminoPartida = partidaTerminada(partida.getId());
            notificarEstadoPartida(partida.getId(), jugador, false, terminoPartida, modoJuego);
        }


        return resultadoRespuestaSiguiente;

    }

//    private ResultadoRespuesta partidaSupervivencia(ResultadoRespuesta resultado) {
//        Long idPartida = resultado.getPartida().getId();
//        Usuario jugador = resultado.getUsuario();
//
//        em.flush();
//        em.clear();
//
//        ResultadoRespuesta resultadoRival = repositorioPartida.obtenerUltimoResultadoRespuestaEnPartidaDeRival(idPartida, jugador);
//
//
//        if (resultadoRival == null ||
//                resultado.getRespuestaSeleccionada() == null ||
//                resultadoRival.getRespuestaSeleccionada() == null ||
//                !resultado.getRespuestaSeleccionada().getId().equals(resultadoRival.getRespuestaSeleccionada().getId())) {
//
//            if(resultado.getRespuestaSeleccionada() != null && resultado.getRespuestaSeleccionada().getId().equals(resultado.getRespuestaCorrecta().getId())) {
//                int xpActual = jugador.getExperiencia() != null ? jugador.getExperiencia() : 0;
//                jugador.setExperiencia(xpActual + 150);
//                resultado.getPartida().setGanador(jugador);
//
//                repositorioPartida.actualizarPartida(resultado.getPartida());
//                repositorioUsuario.modificar(jugador);
//                em.flush();
//            }
//            finalizarPartida(idPartida);
//            return null;
//        }
//
//        SiguientePreguntaSupervivencia siguientePregunta = obtenerPreguntaSupervivencia(idPartida, jugador, resultado);
//        if (siguientePregunta == null) {
//            finalizarPartida(idPartida);
//            return null;
//        }
//
//        Pregunta pregunta = siguientePregunta.getSiguientePregunta();
//        int nuevoOrden = siguientePregunta.getOrden();
//
//        // Crear resultados vacíos para orden anterior de ambos jugadores si faltan
//        int ordenAnterior = nuevoOrden - 1;
//        if (ordenAnterior > 0) {
//            List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(idPartida);
//            for (Usuario u : jugadores) {
//                Pregunta pregAnterior = buscarPreguntaPorOrden(idPartida, ordenAnterior);
//                if (pregAnterior != null) {
//                    ResultadoRespuesta rrAnterior = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, u, ordenAnterior, pregAnterior);
//                    if (rrAnterior == null) {
//                        obtenerOCrearResultadoRespuesta(pregAnterior.getId(), idPartida, u, ordenAnterior);
//                    }
//                }
//            }
//        }
//
//        ResultadoRespuesta existenteJugador = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, jugador, nuevoOrden, pregunta);
//        if (existenteJugador == null) {
//            existenteJugador = partidaTransaccional.crearResultadoRespuestaConOrdenFijo(pregunta.getId(), idPartida, jugador, null, nuevoOrden);
//        }
//
//        ResultadoRespuesta existenteRival = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, resultadoRival.getUsuario(), nuevoOrden, pregunta);
//        if (existenteRival == null) {
//            partidaTransaccional.crearResultadoRespuestaConOrdenFijo(pregunta.getId(), idPartida, resultadoRival.getUsuario(), null, nuevoOrden);
//        }
//
//        em.flush();
//
//        return existenteJugador;
//    }

    private ResultadoRespuesta partidaSupervivencia(ResultadoRespuesta resultado) {
        Long idPartida = resultado.getPartida().getId();
        Usuario jugador = resultado.getUsuario();

        em.flush();
        em.clear();

        ResultadoRespuesta resultadoRival = repositorioPartida.obtenerResultadoRespuestaDeRivalPorOrden(idPartida, jugador, resultado.getOrden());



        Long idRespJugador = resultado.getRespuestaSeleccionada().getId();
        Long idRespRival = resultadoRival.getRespuestaSeleccionada().getId();
        Long idRespCorrecta = resultado.getRespuestaCorrecta().getId();

        boolean jugadorCorrecto = idRespJugador.equals(idRespCorrecta);
        boolean rivalCorrecto = idRespRival.equals(idRespCorrecta);

        // CASO: uno acierta y el otro no → termina
        if (jugadorCorrecto && !rivalCorrecto) {
            jugador.setExperiencia((jugador.getExperiencia() != null ? jugador.getExperiencia() : 0) + 150);
            resultado.getPartida().setGanador(jugador);
            repositorioPartida.actualizarPartida(resultado.getPartida());
            repositorioUsuario.modificar(jugador);
            em.flush();
            finalizarPartida(idPartida);
            return null;
        }

        if (rivalCorrecto && !jugadorCorrecto) {
            Usuario rival = resultadoRival.getUsuario();
            rival.setExperiencia((rival.getExperiencia() != null ? rival.getExperiencia() : 0) + 150);
            resultado.getPartida().setGanador(rival);
            repositorioPartida.actualizarPartida(resultado.getPartida());
            repositorioUsuario.modificar(rival);
            em.flush();
            finalizarPartida(idPartida);
            return null;
        }

        // CASO: ambos correctos o ambos incorrectos → seguir con siguiente pregunta
        SiguientePreguntaSupervivencia siguientePregunta = obtenerPreguntaSupervivencia(idPartida, jugador, resultado);
        if (siguientePregunta == null) {
            finalizarPartida(idPartida);
            return null;
        }

        Pregunta pregunta = siguientePregunta.getSiguientePregunta();
        int nuevoOrden = siguientePregunta.getOrden();

        // Crear resultados vacíos para orden anterior si faltan
        int ordenAnterior = nuevoOrden - 1;
        if (ordenAnterior > 0) {
            List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(idPartida);
            for (Usuario u : jugadores) {
                Pregunta pregAnterior = buscarPreguntaPorOrden(idPartida, ordenAnterior);
                if (pregAnterior != null) {
                    ResultadoRespuesta rrAnterior = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, u, ordenAnterior, pregAnterior);
                    if (rrAnterior == null) {
                        obtenerOCrearResultadoRespuesta(pregAnterior.getId(), idPartida, u, ordenAnterior);
                    }
                }
            }
        }

        ResultadoRespuesta existenteJugador = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, jugador, nuevoOrden, pregunta);
        if (existenteJugador == null) {
            existenteJugador = partidaTransaccional.crearResultadoRespuestaConOrdenFijo(pregunta.getId(), idPartida, jugador, null, nuevoOrden);
        }

        ResultadoRespuesta existenteRival = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, resultadoRival.getUsuario(), nuevoOrden, pregunta);
        if (existenteRival == null) {
            partidaTransaccional.crearResultadoRespuestaConOrdenFijo(pregunta.getId(), idPartida, resultadoRival.getUsuario(), null, nuevoOrden);
        }

        em.flush();

        return existenteJugador;
    }






    @Override
    public boolean chequearAmbosRespondieron(Long p, Usuario jugador, Integer orden) {
        Partida partida = buscarPartidaPorId(p);

        // 1. Validar cantidad
        List<Usuario> jugadores = obtenerJugadoresEnPartida(p);
        if (jugadores.size() != 2) {
            throw new IllegalStateException("Partida " + p + " debe tener 2 jugadores, tiene: " + jugadores.size());
        }

        // 2. Obtener rival
        Usuario rival = jugadores.stream()
                .filter(u -> !u.equals(jugador))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró rival para " + jugador.getEmail()));

        // 3. Traer resultados por orden
        ResultadoRespuesta resultado = repositorioPartida.obtenerResultadoPorOrden(p, jugador, orden);
        ResultadoRespuesta resultadoRival = repositorioPartida.obtenerResultadoPorOrden(p, rival, orden);



        if(resultadoRival == null || resultado == null) {
            return false;
        }
        if(!resultadoRival.getOrden().equals(resultado.getOrden())) {
            return false;
        }

        // si resultado de ambos la respuesta es nula y termino respuesta nula es false, osea q no se envio ninguna respuesta aunq sea -1
        if((resultadoRival.getRespuestaSeleccionada() == null && resultadoRival.getTiempoTerminadoRespuestaNula().equals(Boolean.FALSE))  || (resultado.getRespuestaSeleccionada() == null && resultado.getTiempoTerminadoRespuestaNula().equals(Boolean.FALSE))){
            return false;
        }
        // Chequear que ambos están respondiendo la misma pregunta
        if (!Objects.equals(resultado.getPregunta(), resultadoRival.getPregunta())) {
            return false;
        }


        return true;
    }

    @Override
    public ResultadoRespuesta crearResultadoRespuestaConSiguienteOrden(Long idPregunta, Long idPartida, Usuario usuario, Long idRespuesta) {
        Partida p = buscarPartidaPorId(idPartida);
        ResultadoRespuesta ultimo = repositorioPartida.obtenerUltimoResultadoRespuestaEnPartidaPorJugador(idPartida, usuario);

        int nuevoOrden = (ultimo == null || ultimo.getOrden() == null) ? 1 : ultimo.getOrden() + 1;

        Pregunta preguntaRespondida = buscarPreguntaPorId(idPregunta);
        ResultadoRespuesta existente = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, usuario, nuevoOrden, preguntaRespondida);

        if (existente != null) {
            return existente;
        }

        ResultadoRespuesta resultado = new ResultadoRespuesta();
        resultado.setPartida(p);
        resultado.setPregunta(preguntaRespondida);
        resultado.setUsuario(usuario);
        resultado.setOrden(nuevoOrden);

        Respuesta respuestaSeleccionada = null;
        if (idRespuesta == null) {
            resultado.setTiempoTerminadoRespuestaNula(Boolean.FALSE);
        } else if (idRespuesta == -1) {
            resultado.setTiempoTerminadoRespuestaNula(Boolean.TRUE);
        } else {
            respuestaSeleccionada = buscarRespuestaPorId(idRespuesta);
            resultado.setTiempoTerminadoRespuestaNula(Boolean.FALSE);
        }

        resultado.setRespuestaSeleccionada(respuestaSeleccionada);
        resultado.setRespuestaCorrecta(
                preguntaRespondida.getRespuestas().stream()
                        .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                        .findFirst()
                        .orElse(null)
        );

        try {
            return partidaTransaccional.guardarResultadoConFlush(resultado);
        } catch (PersistenceException e) {
            em.clear();
            return repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, usuario, nuevoOrden, preguntaRespondida);
        }
    }



    @Transactional
    @Override
    public ResultadoRespuesta obtenerOCrearResultadoRespuesta(Long idPregunta, Long idPartida, Usuario usuario, Integer orden) {
        Pregunta pregunta = buscarPreguntaPorId(idPregunta);
        ResultadoRespuesta resultadoExistente = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, usuario, orden, pregunta);

        if (resultadoExistente != null) {
            return resultadoExistente;
        }

        ResultadoRespuesta resultado = new ResultadoRespuesta();
        resultado.setPartida(buscarPartidaPorId(idPartida));
        resultado.setPregunta(pregunta);
        resultado.setUsuario(usuario);
        resultado.setOrden(orden);
        resultado.setTiempoTerminadoRespuestaNula(Boolean.FALSE);
        resultado.setRespuestaSeleccionada(null);
        resultado.setRespuestaCorrecta(
                pregunta.getRespuestas().stream()
                        .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                        .findFirst()
                        .orElse(null)
        );

        try {

            return partidaTransaccional.guardarResultadoConFlush(resultado);
        } catch (PersistenceException ex) {
            // Otro hilo lo insertó al mismo tiempo: lo buscamos de nuevo
            em.clear(); // importante para evitar conflicto de contexto de persistencia
            return repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, usuario, orden, pregunta);
        }
    }


    private ResultadoRespuesta partidaMultijugador(ResultadoRespuesta resultado, HttpServletRequest request) throws UsuarioNoExistente {

        boolean respondioBien = resultado.getRespuestaSeleccionada() != null &&
                resultado.getRespuestaSeleccionada().getId().equals(resultado.getRespuestaCorrecta().getId());

        Partida partida = resultado.getPartida();
        Usuario jugador = resultado.getUsuario();

        CategoriasGanadasEnPartida ganadasUsuario = obtenerCategoriasGanadasDeUsuarioEnPartida(partida, jugador);
        // Verificar si el jugador ganó todas las categorías
        if (ganadasUsuario != null && ganadasUsuario.getCategoriasGanadas() != null && ganadasUsuario.getCategoriasGanadas().size() == CATEGORIA_PREGUNTA.values().length) {
            finalizarPartida(partida.getId());
            notificarEstadoPartida(partida.getId(), jugador, false, true, TIPO_PARTIDA.MULTIJUGADOR);
            servicioMisionesUsuario.completarMisiones(request);
            return null;
        }

        List<Usuario> jugadores = obtenerJugadoresEnPartida(partida.getId());

        // Caso: solo un jugador (esperando rival)
        if (jugadores.size() == 1 && !respondioBien) {
            partida.setTurnoActual(null);
            repositorioPartida.actualizarPartida(partida);
            em.flush();
            notificarEsperandoRival(jugador, null, partida);
            return null;
        }

        // Buscar rival
        Usuario rival = jugadores.stream()
                .filter(u -> !u.getId().equals(jugador.getId()))
                .findFirst()
                .orElse(null);

        if (respondioBien) {

            //suma al total del xp del usuario
//            Integer xpActual = jugador.getXp();
//            if (xpActual == null) {
//                xpActual = 0;
//            }
//            jugador.setXp(xpActual + 10);
            repositorioUsuario.modificar(jugador);

            // suma a los xp del turno para mostrar cuantos gano
            Integer xpTurno = resultado.getXpEnTurno();
            if (xpTurno == null) {
                xpTurno = 0;
            }
            resultado.setXpEnTurno(xpTurno+10);
            repositorioPartida.actualizarResultadoRespuesta(resultado);

            em.flush();

            // El jugador mantiene su turno, el rival espera
            if (rival != null) {
                notificarEsperandoRival(jugador, rival, partida);
            }
            return resultado; // Se mantiene en la pregunta siguiente
        } else {
            // Falló → pasa turno al rival (si no ganó ya)
            if (rival != null) {

                CategoriasGanadasEnPartida ganadasRival = obtenerCategoriasGanadasDeUsuarioEnPartida(partida, rival);
                boolean rivalGano = false;

                if (ganadasRival != null && ganadasRival.getCategoriasGanadas() != null) {
                    rivalGano = ganadasRival.getCategoriasGanadas().size() == CATEGORIA_PREGUNTA.values().length;
                }
                if (rivalGano) {
                    finalizarPartida(partida.getId());
                    notificarEstadoPartida(partida.getId(), jugador, false, true, TIPO_PARTIDA.MULTIJUGADOR);
                    return null;
                }

                partida.setTurnoActual(rival);
                repositorioPartida.actualizarPartida(partida);
                em.flush();
                notificarTurno(rival, jugador, partida);
                notificarEsperandoRival(jugador, rival, partida);
            }
        }

        return null;
    }
    // --------------------------------- WEB SOCKET --------------------------------- //

    private void notificarTurno(Usuario jugadorConTurno, Usuario rival, Partida partida) {
        EstadoPartidaDTO dto = new EstadoPartidaDTO();
        dto.setEstado("tu_turno");
        dto.setMensaje("Es tu turno!");
        dto.setIdPartida(partida.getId());
        dto.setUsuarioId(jugadorConTurno.getId());
        dto.setNombreRival(rival.getNombreUsuario()); // el que espera
        dto.setAvatarUrlRival(repositorioUsuario.obtenerImagenAvatarSeleccionado(rival.getId()));
        messagingTemplate.convertAndSendToUser(jugadorConTurno.getNombreUsuario(), "/queue/partida", dto);
    }

    private void notificarEsperandoRival(Usuario jugadorEsperando, Usuario rival, Partida partida) {
        EstadoPartidaDTO dto = new EstadoPartidaDTO();
        dto.setEstado("esperando_rival");
        dto.setMensaje("Esperando a rival...");
        dto.setIdPartida(partida.getId());
        if (rival != null) {
            dto.setNombreRival(rival.getNombreUsuario());
            dto.setAvatarUrlRival(repositorioUsuario.obtenerImagenAvatarSeleccionado(rival.getId()));
        } else {
            dto.setNombreRival("?");
            dto.setAvatarUrlRival(null);
        }
        messagingTemplate.convertAndSendToUser(jugadorEsperando.getNombreUsuario(), "/queue/partida", dto);
    }


    @Override
    @Transactional
    public void notificarEstadoPartida(Long idPartida, Usuario quienRespondio, boolean ambosRespondieron, boolean terminoPartida, TIPO_PARTIDA modoJuego) {



        List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(idPartida);

        for (Usuario jugador : jugadores) {
            EstadoPartidaDTO dto = new EstadoPartidaDTO();
            if (modoJuego == TIPO_PARTIDA.SUPERVIVENCIA) {

                if (terminoPartida) {
                    dto.setEstado("finalizado");
                    dto.setMensaje("La partida ha finalizado.");
                    messagingTemplate.convertAndSendToUser(jugador.getNombreUsuario(), "/queue/partida", dto);
                } else if (ambosRespondieron) {
                    dto.setAvanzarAutomaticamente(true);
                    dto.setMensaje("Siguiente pregunta...");
                    messagingTemplate.convertAndSendToUser(jugador.getNombreUsuario(), "/queue/partida", dto);
                } else if (jugador.equals(quienRespondio) && !ambosRespondieron) {
                    dto.setEstado("respondio");
                    dto.setMensaje("Esperando que responda tu rival...");
                    messagingTemplate.convertAndSendToUser(jugador.getNombreUsuario(), "/queue/partida", dto);
                }

            } else if (modoJuego == TIPO_PARTIDA.MULTIJUGADOR) {

                if (terminoPartida) {
                    dto.setEstado("finalizado");
                    dto.setMensaje("La partida ha finalizado.");
                    messagingTemplate.convertAndSendToUser(jugador.getNombreUsuario(), "/queue/partida", dto);
                }
            }

        }

    }

    @Transactional
    @Override
    public ResultadoRespuesta crearResultadoRespuestaParaMultijugador(Long idPartida, Usuario usuario, Long idPregunta, Long idRespuesta) {
        Partida p = buscarPartidaPorId(idPartida);

        Pregunta preguntaRespondida = buscarPreguntaPorId(idPregunta);

        ResultadoRespuesta resultado = new ResultadoRespuesta();
        resultado.setPartida(p);
        resultado.setPregunta(preguntaRespondida);
        resultado.setUsuario(usuario);

        Respuesta respuestaSeleccionada = null;

        if (idRespuesta == null) {
            resultado.setTiempoTerminadoRespuestaNula(Boolean.FALSE);
        } else if (idRespuesta == -1) {
            resultado.setTiempoTerminadoRespuestaNula(Boolean.TRUE);
        } else {
            respuestaSeleccionada = buscarRespuestaPorId(idRespuesta);
            resultado.setTiempoTerminadoRespuestaNula(Boolean.FALSE);
        }

        resultado.setRespuestaSeleccionada(respuestaSeleccionada);

        resultado.setRespuestaCorrecta(
                preguntaRespondida.getRespuestas().stream()
                        .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                        .findFirst()
                        .orElse(null)
        );


        repositorioPartida.guardarResultadoRespuesta(resultado);
        em.flush();

        return resultado;
    }


    //web soquets, logica de las preguntas etc
}