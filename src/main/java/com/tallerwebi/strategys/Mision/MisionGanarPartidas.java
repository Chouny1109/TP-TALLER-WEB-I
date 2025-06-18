package com.tallerwebi.strategys.Mision;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA_JUGADOR;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.model.UsuarioPartida;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MisionGanarPartidas implements EstrategiaMision {

    private static final int CANTIDAD_DE_PARTIDAS_GANADAS = 3;

    private final RepositorioPartida repositorioPartida;
    private final RepositorioMisionUsuario repositorioMisionUsuario;

    @Autowired
    public MisionGanarPartidas(RepositorioPartida repositorioPartida, RepositorioMisionUsuario repositorioMisionUsuario) {
        this.repositorioPartida = repositorioPartida;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {
        if (usuario != null && usuarioMision != null) {
            LocalDateTime fecha = LocalDateTime.now();
            List<UsuarioPartida> usuarioPartidas = this.repositorioPartida.
                    obtenerLasPartidasDelUsuarioParaDeterminadaFecha(usuario.getId(), fecha);

            if (usuarioPartidas.isEmpty()) {
                return;
            }

            int cantidadGanadas = (int) usuarioPartidas.stream().filter(
                    partida -> partida.getEstado().equals(ESTADO_PARTIDA_JUGADOR.VICTORIA)
            ).count();

            if (cantidadGanadas >= CANTIDAD_DE_PARTIDAS_GANADAS) {
                usuarioMision.setCompletada(Boolean.TRUE);
                this.repositorioMisionUsuario.save(usuarioMision);
            }
        }
    }
}
