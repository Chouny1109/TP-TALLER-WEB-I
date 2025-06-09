package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.repository.impl.RepositorioPartidaImpl;
import com.tallerwebi.service.ServicioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ServicioPartidaImpl implements ServicioPartida {
    private final RepositorioPartida repositorioPartida;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, SimpMessagingTemplate messagingTemplate) {
        this.repositorioPartida = repositorioPartida;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego) {
        Partida partidaExistente = repositorioPartida.obtenerPartidaAbiertaPorModo(modoJuego);

        if (partidaExistente != null) {
            partidaExistente.getUsuarios().add(jugador);
            partidaExistente.setEstadoPartida(ESTADO_PARTIDA.EN_CURSO);
            this.repositorioPartida.actualizarPartida(partidaExistente);

            // Notificar al jugador 1 que el jugador 2 se unió
            Usuario jugador1 = partidaExistente.getUsuarios().get(0);
            Usuario jugador2 = jugador;

            messagingTemplate.convertAndSendToUser(
                    jugador1.getNombreUsuario(), // usuario destino
                    "/queue/rivalEncontrado",    // cola privada
                    jugador2                     // contenido mensaje (rival)
            );

            // También podés notificar al jugador 2 con jugador 1
            messagingTemplate.convertAndSendToUser(
                    jugador2.getNombreUsuario(),
                    "/queue/rivalEncontrado",
                    jugador1
            );

            return partidaExistente;
        } else {
            Partida nuevaPartida = new Partida();
            nuevaPartida.setTipo(modoJuego);
            nuevaPartida.setUsuarios(new ArrayList<>());
            nuevaPartida.getUsuarios().add(jugador);
            this.repositorioPartida.guardarPartida(nuevaPartida);
            return nuevaPartida;
        }
    }


    private void partidaSupervivencia() {
    }

    private void partidaMultijugador() {
    }
    //web soquets, logica de las preguntas etc
}
