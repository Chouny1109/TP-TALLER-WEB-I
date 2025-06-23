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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    @Transactional
    public Partida buscarPartidaPorId(Long idPartida){
        return repositorioPartida.buscarPartidaPorId(idPartida);
    }

    @Override
    @Transactional
    public Partida obtenerPreguntaSupervivencia(Long idPartida){
        List<Usuario> jugadores = repositorioPartida.obtenerJugadoresDePartida(idPartida);
        Pregunta pregunta = this.repositorioPregunta.obtenerPreguntaSupervivencia(jugadores);
        Partida p = repositorioPartida.buscarPartidaPorId(idPartida);
        p.setPreguntaActual(pregunta);
        repositorioPartida.actualizarPartida(p);
        em.flush();
        return p;
    }

    @Override
    @Transactional
    public void finalizarPartida(Long idPartida) {
        Partida p = repositorioPartida.buscarPartidaPorId(idPartida);
        p.setEstadoPartida(ESTADO_PARTIDA.FINALIZADA); // << Usar solo esto
        p.setPreguntaActual(null);
        repositorioPartida.actualizarPartida(p); // << Solo este
    }

    @PersistenceContext
    private EntityManager em;
    // Método para chequear si la partida terminó
    @Transactional
    public boolean partidaTerminada(Long idPartida) {
        Partida p = repositorioPartida.buscarPartidaPorId(idPartida);
        em.refresh(p);
        return p.isFinalizada() || p.getPreguntaActual() == null;
    }

    @Override
    @Transactional
    public Pregunta validarRespuesta(Long idRespuestaSeleccionada, Long idPartida, TIPO_PARTIDA modoJuego, Usuario jugador) {
        ResultadoRespuesta resultado = repositorioPartida.obtenerResultadoRespuestaEnPartidaPorJugador(idPartida, jugador);
        if(resultado == null) {
            resultado = new ResultadoRespuesta();
            resultado.setPartida(buscarPartidaPorId(idPartida));
            resultado.setUsuario(jugador);
            repositorioPartida.guardarResultadoRespuesta(resultado);
        }

        Respuesta respuestaSeleccionada = null;
        if (idRespuestaSeleccionada != -1) {
            respuestaSeleccionada = repositorioPregunta.buscarRespuestaPorId(idRespuestaSeleccionada);
        }
        Pregunta pregunta = buscarPartidaPorId(idPartida).getPreguntaActual();

        resultado.setPregunta(pregunta);
        resultado.setRespuestaSeleccionada(respuestaSeleccionada);
        resultado.setRespuestaCorrecta(
                pregunta.getRespuestas().stream()
                        .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                        .findFirst()
                        .orElse(null)
        );

        repositorioPartida.actualizarResultadoRespuesta(resultado);

        boolean ambosRespondieron = chequearAmbosRespondieron(idPartida, jugador);

        Pregunta siguientePregunta = null;

        if (ambosRespondieron) {
            if (modoJuego == TIPO_PARTIDA.SUPERVIVENCIA) {
                Partida actualizada = partidaSupervivencia(resultado);
                if (actualizada != null && actualizada.getPreguntaActual() != null) {
                    siguientePregunta = actualizada.getPreguntaActual();
                }
            } else if (modoJuego == TIPO_PARTIDA.MULTIJUGADOR) {
                siguientePregunta = partidaMultijugador(resultado);
            }
        }



        boolean terminoPartida = partidaTerminada(idPartida) || (siguientePregunta == null && ambosRespondieron);

        // Notificar estado a los jugadores, indicándoles si avanzar o esperar
        notificarEstadoPartida(idPartida, jugador, ambosRespondieron, terminoPartida);

        return siguientePregunta;
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

    private Partida partidaSupervivencia(ResultadoRespuesta resultado) {
        ResultadoRespuesta resultadoRival = repositorioPartida.obtenerResultadoRespuestaEnPartidaDeRival(resultado.getPartida(), resultado.getUsuario());

        if (resultadoRival != null && resultado.getPregunta().equals(resultadoRival.getPregunta())) {
            boolean respondioBien = resultado.respondioBien();
            boolean respondioBienRival = resultadoRival.respondioBien();

            if ((respondioBien && respondioBienRival) || (!respondioBien && !respondioBienRival)) {
                // Ambos respondieron igual (bien o mal) → nueva pregunta
                return obtenerPreguntaSupervivencia(resultado.getPartida().getId());
            } else {
                // Uno bien y otro mal → termina partida
                resultado.getPartida().setPreguntaActual(null);
                repositorioPartida.actualizarPartida(resultado.getPartida());
                finalizarPartida(resultado.getPartida().getId());
                return null;
            }
        }
        return null;
    }


    private Pregunta partidaMultijugador(ResultadoRespuesta resultado) {
      return null;
    }

    @Transactional
    @Override
    public boolean chequearAmbosRespondieron(Long idPartida, Usuario jugador) {
        Partida partida = buscarPartidaPorId(idPartida);
        ResultadoRespuesta resultado = repositorioPartida.obtenerResultadoRespuestaEnPartidaPorJugador(idPartida, jugador);
        ResultadoRespuesta resultadoRival = repositorioPartida.obtenerResultadoRespuestaEnPartidaDeRival(partida, jugador);

        // Si alguno no respondió aún o no seleccionó respuesta, no avanzar
        if (resultado == null || resultado.getRespuestaSeleccionada() == null) return false;
        if (resultadoRival == null || resultadoRival.getRespuestaSeleccionada() == null) return false;

        // Chequear que ambos están respondiendo la misma pregunta
        if (!Objects.equals(resultado.getPregunta(), resultadoRival.getPregunta())) return false;

        return true;
    }



    //web soquets, logica de las preguntas etc
}
