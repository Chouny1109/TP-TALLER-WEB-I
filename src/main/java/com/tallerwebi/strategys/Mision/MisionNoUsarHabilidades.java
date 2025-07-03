package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuarioHabilidadPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MisionNoUsarHabilidades implements EstrategiaMision {

    private final RepositorioMisionUsuario repositorioMisionUsuario;
    private final RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida;

    @Autowired
    public MisionNoUsarHabilidades(RepositorioMisionUsuario repositorioMisionUsuario, RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida) {
        this.repositorioMisionUsuario = repositorioMisionUsuario;
        this.repositorioUsuarioHabilidadPartida = repositorioUsuarioHabilidadPartida;

    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {

        if (usuario != null) {
            boolean tienePartidasGanadasSinHabilidades = this.repositorioUsuarioHabilidadPartida.
                    elUsuarioTienePartidasGanadasSinUsarHabilidades(usuario.getId());

            if (tienePartidasGanadasSinHabilidades) {
                usuarioMision.setProgreso(usuarioMision.getProgreso() + 1);
                usuarioMision.setCompletada(Boolean.TRUE);
                this.repositorioMisionUsuario.save(usuarioMision);
            }
        }
    }
}
