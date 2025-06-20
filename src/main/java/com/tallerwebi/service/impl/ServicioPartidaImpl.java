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

import javax.transaction.Transactional;
import java.util.List;

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
    public List<Usuario> obtenerJugadoresEnPartida(Long id){
        return this.repositorioPartida.obtenerJugadoresDePartida(id);
    }

    @Override
    @Transactional
    public void finalizarPartida(Long idPartida) {
        this.repositorioPartida.finalizarPartida(idPartida);
    }

    @Override
    public Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario) {
       return this.repositorioPregunta.obtenerPregunta(categoria,idUsuario);

    }

    private void partidaSupervivencia() {
    }

    private void partidaMultijugador() {
    }
    //web soquets, logica de las preguntas etc
}
