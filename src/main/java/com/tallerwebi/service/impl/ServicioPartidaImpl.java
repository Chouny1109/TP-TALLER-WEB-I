package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioDTO;
import com.tallerwebi.model.UsuarioPartida;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.repository.impl.RepositorioPartidaImpl;
import com.tallerwebi.service.ServicioPartida;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class ServicioPartidaImpl implements ServicioPartida {
    @Autowired
    private SessionFactory sessionFactory;
    private final RepositorioPartida repositorioPartida;
    private final RepositorioUsuario repositorioUsuario;
    private final SimpMessagingTemplate messagingTemplate;
    private final Object lock = new Object();

    @Autowired
    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, RepositorioUsuario repositorioUsuario,SimpMessagingTemplate messagingTemplate) {
        this.repositorioPartida = repositorioPartida;
        this.messagingTemplate = messagingTemplate;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional
    public Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego) {
        synchronized(lock) {
            for (int intento = 0; intento < 2; intento++) {
                Partida partidaExistente = repositorioPartida.obtenerPartidaAbiertaPorModo(modoJuego);

                if (partidaExistente != null) {
                    // Revalidamos el estado actual antes de intentar unirse
                    if (!partidaExistente.getEstadoPartida().equals(ESTADO_PARTIDA.ABIERTA)) {
                        // Alguien más la tomó justo antes, reintentar
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Hilo interrumpido mientras esperaba para buscar rival");
                        }

                        continue;
                    }

                    // Agregamos al nuevo jugador a la partida
                    UsuarioPartida up = new UsuarioPartida(jugador, partidaExistente);
                    repositorioPartida.agregarUsuarioPartidaRelacion(up);
                    sessionFactory.getCurrentSession().flush();

                    // Cambiamos estado a EN_CURSO porque ya hay 2 jugadores
                    partidaExistente.setEstadoPartida(ESTADO_PARTIDA.EN_CURSO);
                    repositorioPartida.actualizarPartida(partidaExistente);

                    // Obtenemos el rival (el otro jugador)
                    Usuario jugador1 = null;
                    int reintentos = 0;
                    while (jugador1 == null && reintentos < 5) {
                        sessionFactory.getCurrentSession().flush(); // aseguramos que la sesión esté sincronizada
                        jugador1 = repositorioPartida.obtenerRivalDePartida(partidaExistente.getId(), jugador.getId());
                        if (jugador1 == null) {
                            try {
                                Thread.sleep(100); // esperar un poco para dar tiempo al otro hilo
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt(); // buena práctica
                            }
                            reintentos++;
                        }
                    }
                    Usuario jugador2 = jugador;

                    if (jugador1 != null) {
                        String avatarJug1 = repositorioUsuario.obtenerImagenAvatarSeleccionado(jugador1.getId());
                        String avatarJug2 = repositorioUsuario.obtenerImagenAvatarSeleccionado(jugador2.getId());
                        UsuarioDTO jugador1DTO = new UsuarioDTO(jugador1, avatarJug1);
                        UsuarioDTO jugador2DTO = new UsuarioDTO(jugador2, avatarJug2);

                        messagingTemplate.convertAndSendToUser(
                                jugador1.getNombreUsuario(),
                                "/queue/rivalEncontrado",
                                jugador2DTO
                        );
                        messagingTemplate.convertAndSendToUser(
                                jugador2.getNombreUsuario(),
                                "/queue/rivalEncontrado",
                                jugador1DTO
                        );
                    }

                    return partidaExistente;
                } else {
                    // Esperamos un poco antes de crear, por si otro usuario está justo en eso
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                }
            }

            // Ninguna partida válida disponible, creamos nueva
            Partida nuevaPartida = new Partida();
            nuevaPartida.setTipo(modoJuego);
            nuevaPartida.setEstadoPartida(ESTADO_PARTIDA.ABIERTA);

            this.repositorioPartida.guardarPartida(nuevaPartida);

            UsuarioPartida up = new UsuarioPartida(jugador, nuevaPartida);
            this.repositorioPartida.agregarUsuarioPartidaRelacion(up);
            sessionFactory.getCurrentSession().flush();

            return nuevaPartida;
        }
    }


    private void partidaSupervivencia() {
    }

    private void partidaMultijugador() {
    }
    //web soquets, logica de las preguntas etc
}
