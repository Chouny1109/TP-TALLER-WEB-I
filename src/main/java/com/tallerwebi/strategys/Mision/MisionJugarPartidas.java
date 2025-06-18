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
    private static final Integer PARTIDAS_NECESARIAS = 5;
    private final RepositorioMisionUsuario repositorioMisionUsuario;

    public MisionJugarPartidas(RepositorioPartida repositorioPartida, RepositorioMisionUsuario repositorioMisionUsuario) {
        this.repositorioPartida = repositorioPartida;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {
        LocalDate fecha = LocalDate.now();
        Integer partidasJugadas = this.repositorioPartida.obtenerCantidadDePartidasJugadasParaLaFecha(usuario.getId(), fecha);

        if (partidasJugadas >= PARTIDAS_NECESARIAS) {
            usuarioMision.setCompletada(Boolean.TRUE);
            this.repositorioMisionUsuario.save(usuarioMision);
        }
    }
}
