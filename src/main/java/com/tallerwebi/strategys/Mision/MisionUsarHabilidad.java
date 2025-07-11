package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioUsuarioHabilidadPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MisionUsarHabilidad implements EstrategiaMision {

    private final RepositorioMisionUsuario repositorioMisionUsuario;
    private final RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida;

    @Autowired
    public MisionUsarHabilidad(RepositorioMisionUsuario repositorioMisionUsuario,
                               RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida) {
        this.repositorioMisionUsuario = repositorioMisionUsuario;
        this.repositorioUsuarioHabilidadPartida = repositorioUsuarioHabilidadPartida;
    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {

        if (usuario != null) {
            LocalDate fecha = LocalDate.now();
            boolean cumplio = this.repositorioUsuarioHabilidadPartida.
                    obtenerHabilidadesUsadasParaLaFecha(usuario.getId(), fecha);

            if (cumplio) {
                usuarioMision.setProgreso(usuarioMision.getProgreso() + 1);
                usuarioMision.setCompletada(Boolean.TRUE);
                this.repositorioMisionUsuario.save(usuarioMision);
            }
        }
    }
}
