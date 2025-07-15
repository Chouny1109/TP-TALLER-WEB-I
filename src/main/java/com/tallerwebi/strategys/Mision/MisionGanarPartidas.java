package com.tallerwebi.strategys.Mision;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA_JUGADOR;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.model.UsuarioPartida;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioNivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class MisionGanarPartidas extends MisionPlantilla {

    private final RepositorioPartida repositorioPartida;

    public MisionGanarPartidas(RepositorioMisionUsuario repositorioMisionUsuario, RepositorioUsuario repositorioUsuario, ServicioNivel servicioNivel, RepositorioPartida repositorioPartida) {
        super(repositorioMisionUsuario, repositorioUsuario, servicioNivel);
        this.repositorioPartida = repositorioPartida;
    }

    private Integer obtenerCantidadDePartidasGanadas(Usuario usuario) {
        LocalDate fecha = LocalDate.now();
        Long id = usuario.getId();
        return this.repositorioPartida.obtenerCantidadDePartidasGanadasParaLaFecha(id, fecha);
    }

    @Override
    protected boolean verificarCumplimiento(Usuario usuario, UsuarioMision usuarioMision) {
        return obtenerCantidadDePartidasGanadas(usuario) >= usuarioMision.getMision().getObjetivo();
    }

    @Override
    protected void actualizarProgreso(Usuario usuario, UsuarioMision usuarioMision) {
        Integer cantidadPartidasGanadas = obtenerCantidadDePartidasGanadas(usuario);

        if (cantidadPartidasGanadas > usuarioMision.getProgreso()) {
            usuarioMision.setProgreso(cantidadPartidasGanadas);
        }

        if (cantidadPartidasGanadas >= usuarioMision.getMision().getObjetivo()) {
            usuarioMision.setCompletada(Boolean.TRUE);
        }
    }
}
