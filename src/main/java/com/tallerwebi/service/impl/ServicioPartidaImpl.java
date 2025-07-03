package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioPregunta;
import com.tallerwebi.repository.RepositorioUsuario;
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
    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, RepositorioUsuario repositorioUsuario, RepositorioPregunta repositorioPregunta,SimpMessagingTemplate messagingTemplate) {
        this.repositorioPartida = repositorioPartida;
        this.messagingTemplate = messagingTemplate;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioPregunta = repositorioPregunta;
    }
    @Override
    @Transactional
    public Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego) {
        synchronized(lock) {
            Partida partida;

            if (repositorioPartida.jugadorEstaJugando(jugador.getId())) {
                partida = repositorioPartida.obtenerPartidaActivaDeJugador(jugador.getId());
            } else {
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


    /*@Override
    public Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario) {
     Pregunta pregunta = this.repositorioPregunta.obtenerPregunta(categoria,idUsuario);
        if (pregunta != null) {
            List<Respuesta> mezcladas = new ArrayList<>(pregunta.getRespuestas());
            Collections.shuffle(mezcladas);
            pregunta.setRespuestas(new HashSet<>(mezcladas)); // si seguís usando Set
        }
        return pregunta;
    }*/



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

    private static final ConcurrentHashMap<Long, Object> locksPorPartida = new ConcurrentHashMap<>();
    @Autowired
    private ServicioPartidaTransaccional partidaTransaccional;

    @Transactional
    @Override
    public SiguientePreguntaSupervivencia obtenerPreguntaSupervivencia(Long idPartida, Usuario usuario, ResultadoRespuesta resultadoUltimo) {
        Object lock = locksPorPartida.computeIfAbsent(idPartida, k -> new Object());

        // 🔥 CALCULAR EL ORDEN FUERA DEL LOCK
        Integer ordenActual = repositorioPartida.obtenerMaxOrdenSiguientePregunta(idPartida);
        Integer ordenBuscado = (ordenActual == null) ? 1 : ordenActual + 1;

        synchronized (lock) {
            Partida partida = buscarPartidaPorId(idPartida);

            em.flush();
            em.clear();

            // 🔁 Chequear si ya existe la pregunta con ese orden
            SiguientePreguntaSupervivencia existente = repositorioPartida.obtenerSiguientePreguntaEntidad(partida, ordenBuscado);
            if (existente != null) {
                return existente;
            }

            List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(idPartida);
            Pregunta nuevaPregunta = repositorioPregunta.obtenerPreguntaSupervivencia(jugadores);

            if (nuevaPregunta == null) {
                finalizarPartida(idPartida);
                return null;
            }

            SiguientePreguntaSupervivencia siguiente = new SiguientePreguntaSupervivencia(
                    partida, jugadores.get(0), jugadores.get(1), ordenBuscado, nuevaPregunta
            );

            try {
                partidaTransaccional.guardarSiguientePregunta(siguiente);
            } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                return partidaTransaccional.obtenerPreguntaExistenteConNuevaTransaccion(partida, ordenBuscado);
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
    @Transactional
    public ResultadoRespuesta validarRespuesta(ResultadoRespuesta resultado, TIPO_PARTIDA modoJuego) {

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




        boolean ambosRespondieron = chequearAmbosRespondieron(partida.getId(), jugador, orden);

        ResultadoRespuesta resultadoRespuestaSiguiente = null;
        if(ambosRespondieron){
            if (modoJuego == TIPO_PARTIDA.SUPERVIVENCIA) {
                resultadoRespuestaSiguiente = partidaSupervivencia(resultado);
            }
        }
        if (modoJuego == TIPO_PARTIDA.MULTIJUGADOR) {
            resultadoRespuestaSiguiente = partidaMultijugador(resultado);
        }



        // Fallback si no avanzó la partida


        boolean terminoPartida = partidaTerminada(partida.getId()) ||
                (resultadoRespuestaSiguiente == null && ambosRespondieron);

        notificarEstadoPartida(partida.getId(), jugador, ambosRespondieron, terminoPartida);

        return resultadoRespuestaSiguiente;
    }

    private ResultadoRespuesta partidaMultijugador(ResultadoRespuesta resultado) {
        return null;
    }


    @Override
    @Transactional
    public void notificarEstadoPartida(Long idPartida, Usuario quienRespondio, boolean ambosRespondieron, boolean terminoPartida) {
        List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(idPartida);

        for (Usuario jugador : jugadores) {
            EstadoPartidaDTO dto = new EstadoPartidaDTO();

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
        }
    }

    @Override
    public Pregunta buscarPreguntaPorId(Long idPregunta) {
        return repositorioPregunta.buscarPreguntaPorId(idPregunta);
    }

    private ResultadoRespuesta partidaSupervivencia(ResultadoRespuesta resultado) {
        Long idPartida = resultado.getPartida().getId();
        Usuario jugador = resultado.getUsuario();

        em.flush();
        em.clear();
        // Obtener resultado del rival actualizado
        ResultadoRespuesta resultadoRival = repositorioPartida.obtenerUltimoResultadoRespuestaEnPartidaDeRival(idPartida, jugador);

        // Validar que ambos respondieron lo mismo
        if (resultadoRival == null ||
                resultado.getRespuestaSeleccionada() == null ||
                resultadoRival.getRespuestaSeleccionada() == null ||
                !resultado.getRespuestaSeleccionada().getId().equals(resultadoRival.getRespuestaSeleccionada().getId())) {

            finalizarPartida(idPartida);
            return null;
        }

        // Obtener o generar la siguiente pregunta con orden siguiente
        SiguientePreguntaSupervivencia siguientePregunta = obtenerPreguntaSupervivencia(idPartida, jugador, resultado);
        if (siguientePregunta == null) {
            finalizarPartida(idPartida);
            return null;
        }

        Pregunta pregunta = siguientePregunta.getSiguientePregunta();
        int nuevoOrden = siguientePregunta.getOrden();

        // Asegurarse de crear ResultadoRespuesta para ambos jugadores con ese orden y pregunta
        ResultadoRespuesta existenteJugador = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, jugador, nuevoOrden, pregunta);
        if (existenteJugador == null) {
            existenteJugador = crearResultadoRespuestaConOrdenFijo(pregunta.getId(), idPartida, jugador, null, nuevoOrden);
        }

        ResultadoRespuesta existenteRival = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, resultadoRival.getUsuario(), nuevoOrden, pregunta);
        if (existenteRival == null) {
            crearResultadoRespuestaConOrdenFijo(pregunta.getId(), idPartida, resultadoRival.getUsuario(), null, nuevoOrden);
        }

        em.flush();
        // Retornar el del jugador actual, para que siga su flujo
        return existenteJugador;
    }


    @Transactional
    public ResultadoRespuesta crearResultadoRespuestaConOrdenFijo(Long idPregunta, Long idPartida, Usuario usuario, Long idRespuesta, Integer ordenFijo) {
        Partida partida = buscarPartidaPorId(idPartida);
        Pregunta pregunta = buscarPreguntaPorId(idPregunta);

        ResultadoRespuesta resultado = new ResultadoRespuesta();
        resultado.setPartida(partida);
        resultado.setPregunta(pregunta);
        resultado.setUsuario(usuario);
        resultado.setOrden(ordenFijo);

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

        // Guardar respuesta correcta para evaluación posterior
        resultado.setRespuestaCorrecta(
                pregunta.getRespuestas().stream()
                        .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                        .findFirst()
                        .orElse(null)
        );

        repositorioPartida.guardarResultadoRespuesta(resultado);
        em.flush();

        return resultado;
    }

    @Override
    @Transactional
    public boolean chequearAmbosRespondieron(Long p, Usuario jugador, Integer orden) {
        Partida partida = buscarPartidaPorId(p);


        ResultadoRespuesta resultado = repositorioPartida.obtenerUltimoResultadoRespuestaEnPartidaPorJugador(p, jugador);
        ResultadoRespuesta resultadoRival = repositorioPartida.obtenerUltimoResultadoRespuestaEnPartidaDeRival(p, resultado.getUsuario());


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
    @Transactional
    public ResultadoRespuesta crearResultadoRespuestaConSiguienteOrden(Long idPregunta, Long idPartida, Usuario usuario, Long idRespuesta) {

        Partida p = buscarPartidaPorId(idPartida);
        ResultadoRespuesta ultimo = repositorioPartida.obtenerUltimoResultadoRespuestaEnPartidaPorJugador(idPartida, usuario);
        int nuevoOrden = (ultimo == null) ? 1 : ultimo.getOrden() + 1;

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

        resultado.setOrden(nuevoOrden);

        repositorioPartida.guardarResultadoRespuesta(resultado);
        em.flush();

        return resultado;
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
    //web soquets, logica de las preguntas etc
}