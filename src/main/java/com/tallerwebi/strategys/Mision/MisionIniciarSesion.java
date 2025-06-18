package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuario;
import org.springframework.stereotype.Component;

@Component
public class MisionIniciarSesion implements EstrategiaMision {

    private RepositorioMisionUsuario repositorioMisionUsuario;

    public MisionIniciarSesion(RepositorioMisionUsuario repositorioMisionUsuario) {
        this.repositorioMisionUsuario = repositorioMisionUsuario;
    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {
        if (usuario != null) {
            usuarioMision.setCompletada(Boolean.TRUE);
            this.repositorioMisionUsuario.save(usuarioMision);
        }
    }
}
