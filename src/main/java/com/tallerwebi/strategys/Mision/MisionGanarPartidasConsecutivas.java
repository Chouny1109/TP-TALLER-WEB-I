package com.tallerwebi.strategys.Mision;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA_JUGADOR;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.model.UsuarioPartida;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioNivel;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MisionGanarPartidasConsecutivas extends MisionPlantilla {

    private final RepositorioPartida repositorioPartida;

    public MisionGanarPartidasConsecutivas(RepositorioMisionUsuario repositorioMisionUsuario, RepositorioUsuario repositorioUsuario, ServicioNivel servicioNivel, RepositorioPartida repositorioPartida) {
        super(repositorioMisionUsuario, repositorioUsuario, servicioNivel);
        this.repositorioPartida = repositorioPartida;
    }

    private Integer obtenerCantidadDePartidasGanadasConsecutivas(Usuario usuario) {
        Long id = usuario.getId();
        LocalDateTime fecha = LocalDateTime.now();
        List<UsuarioPartida> partidas = repositorioPartida.obtenerLasPartidasDelUsuarioParaDeterminadaFecha(id, fecha);

        int rachaActual = 0;
        int rachaMaxima = 0;

        for (UsuarioPartida partida : partidas) {
            if (partida.getEstado().equals(ESTADO_PARTIDA_JUGADOR.VICTORIA)) {
                rachaActual++;
                rachaMaxima = Math.max(rachaMaxima, rachaActual);
            } else {
                rachaActual = 0;
            }
        }

        return rachaMaxima;
    }

    @Override
    protected boolean verificarCumplimiento(Usuario usuario, UsuarioMision usuarioMision) {
        return obtenerCantidadDePartidasGanadasConsecutivas(usuario) >= usuarioMision.getMision().getObjetivo();
    }

    @Override
    protected void actualizarProgreso(Usuario usuario, UsuarioMision usuarioMision) {
        Integer cantidadPartidasGanadasConsecutivas = obtenerCantidadDePartidasGanadasConsecutivas(usuario);
        if (cantidadPartidasGanadasConsecutivas > usuarioMision.getProgreso()) {
            usuarioMision.setProgreso(cantidadPartidasGanadasConsecutivas);
        }
        if (cantidadPartidasGanadasConsecutivas >= usuarioMision.getMision().getObjetivo()) {
            usuarioMision.setProgreso(usuarioMision.getProgreso() + 1);
            usuarioMision.setCompletada(Boolean.TRUE);
        }
    }
}
