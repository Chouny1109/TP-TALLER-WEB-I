package com.tallerwebi.strategys.Mision;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA_JUGADOR;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.model.UsuarioPartida;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MisionGanarPartidasConsecutivas implements EstrategiaMision {

    private static final int CANTIDAD_DE_VICTORIAS_CONSECUTIVAS = 5;
    private final RepositorioPartida repositorioPartida;
    private final RepositorioMisionUsuario repositorioMisionUsuario;

    public MisionGanarPartidasConsecutivas(RepositorioPartida repositorioPartida,
                                           RepositorioMisionUsuario repositorioMisionUsuario) {
        this.repositorioPartida = repositorioPartida;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {

        if (usuario != null) {
            int contador = 0;
            LocalDateTime fecha = LocalDateTime.now();
            List<UsuarioPartida> usuarioPartidas = this.repositorioPartida.
                    obtenerLasPartidasDelUsuarioParaDeterminadaFecha(usuario.getId(), fecha);

            for (UsuarioPartida partida : usuarioPartidas) {
                if (partida.getEstado().equals(ESTADO_PARTIDA_JUGADOR.VICTORIA)) {
                    contador++;

                    if (contador == CANTIDAD_DE_VICTORIAS_CONSECUTIVAS) {
                        usuarioMision.setCompletada(Boolean.TRUE);
                        this.repositorioMisionUsuario.save(usuarioMision);
                        return;
                    }
                } else {
                    contador = 0;
                }
            }
        }
    }
}

