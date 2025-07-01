package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MisionJugarPartidas implements EstrategiaMision {

    private final RepositorioPartida repositorioPartida;
    private final RepositorioMisionUsuario repositorioMisionUsuario;

    public MisionJugarPartidas(RepositorioPartida repositorioPartida,
                               RepositorioMisionUsuario repositorioMisionUsuario) {
        this.repositorioPartida = repositorioPartida;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {
        LocalDate fecha = LocalDate.now();
        Integer partidasJugadas = this.repositorioPartida.
                obtenerCantidadDePartidasJugadasParaLaFecha(usuario.getId(), fecha);
        Integer objetivo = usuarioMision.getMision().getCantidad();
        Integer progreso = usuarioMision.getProgreso();

        if (partidasJugadas > progreso) {
            usuarioMision.setProgreso(partidasJugadas);
            if (partidasJugadas >= objetivo) {
                usuarioMision.setCompletada(Boolean.TRUE);
            }
            this.repositorioMisionUsuario.save(usuarioMision);
        }
    }
}
