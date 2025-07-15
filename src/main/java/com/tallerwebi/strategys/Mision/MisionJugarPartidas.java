package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioNivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MisionJugarPartidas extends MisionPlantilla {

    private final RepositorioPartida repositorioPartida;

    public MisionJugarPartidas(RepositorioMisionUsuario repositorioMisionUsuario, RepositorioUsuario repositorioUsuario, ServicioNivel servicioNivel, RepositorioPartida repositorioPartida) {
        super(repositorioMisionUsuario, repositorioUsuario, servicioNivel);
        this.repositorioPartida = repositorioPartida;
    }

    @Override
    protected boolean verificarCumplimiento(Usuario usuario, UsuarioMision usuarioMision) {
        Integer objetivo = usuarioMision.getMision().getObjetivo();
        return obtenerCantidadDePartidasJugadas(usuario) >= objetivo;
    }

    private Integer obtenerCantidadDePartidasJugadas(Usuario usuario) {
        LocalDate fecha = LocalDate.now();
        Long id = usuario.getId();
        return this.repositorioPartida.obtenerCantidadDePartidasJugadasParaLaFecha(id, fecha);
    }

    @Override
    protected void actualizarProgreso(Usuario usuario, UsuarioMision usuarioMision) {
        Integer partidasJugadas = obtenerCantidadDePartidasJugadas(usuario);
        Integer progreso = usuarioMision.getProgreso();
        Integer objetivo = usuarioMision.getMision().getObjetivo();

        if (partidasJugadas > progreso) {
            usuarioMision.setProgreso(partidasJugadas);
        }
        if (partidasJugadas >= objetivo) {
            usuarioMision.setCompletada(Boolean.TRUE);
        }
    }
}
