package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.repository.RepositorioUsuarioHabilidadPartida;
import com.tallerwebi.service.ServicioNivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class MisionNoUsarHabilidades extends MisionPlantilla {

    private final RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida;

    public MisionNoUsarHabilidades(RepositorioMisionUsuario repositorioMisionUsuario, RepositorioUsuario repositorioUsuario, ServicioNivel servicioNivel, RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida) {
        super(repositorioMisionUsuario, repositorioUsuario, servicioNivel);
        this.repositorioUsuarioHabilidadPartida = repositorioUsuarioHabilidadPartida;
    }

    private boolean tieneAlgunaPartidaGanadaSinHabilidades(Usuario usuario) {
        return this.repositorioUsuarioHabilidadPartida.elUsuarioTienePartidasGanadasSinUsarHabilidades(usuario.getId(), LocalDateTime.now());
    }

    @Override
    protected boolean verificarCumplimiento(Usuario usuario, UsuarioMision usuarioMision) {
        return tieneAlgunaPartidaGanadaSinHabilidades(usuario);
    }

    @Override
    protected void actualizarProgreso(Usuario usuario, UsuarioMision usuarioMision) {
        if (tieneAlgunaPartidaGanadaSinHabilidades(usuario)) {
            usuarioMision.setProgreso(usuarioMision.getProgreso() + 1);
            usuarioMision.setCompletada(Boolean.TRUE);
        }
    }
}
