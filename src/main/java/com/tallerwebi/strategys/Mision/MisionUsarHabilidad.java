package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.repository.RepositorioUsuarioHabilidadPartida;
import com.tallerwebi.service.ServicioNivel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class MisionUsarHabilidad extends MisionPlantilla {

    private final RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida;

    public MisionUsarHabilidad(RepositorioMisionUsuario repositorioMisionUsuario, RepositorioUsuario repositorioUsuario, ServicioNivel servicioNivel, RepositorioUsuarioHabilidadPartida repositorioUsuarioHabilidadPartida) {
        super(repositorioMisionUsuario, repositorioUsuario, servicioNivel);
        this.repositorioUsuarioHabilidadPartida = repositorioUsuarioHabilidadPartida;
    }

    @Override
    protected boolean verificarCumplimiento(Usuario usuario, UsuarioMision usuarioMision) {
        return tieneMasDeDosHabilidadesUsadasEnAlgunaPartida(usuario);
    }

    private boolean tieneMasDeDosHabilidadesUsadasEnAlgunaPartida(Usuario usuario) {
        LocalDateTime fecha = LocalDateTime.now();
        Long id = usuario.getId();
        return this.repositorioUsuarioHabilidadPartida.
                obtenerHabilidadesUsadasParaLaFecha(id, fecha);
    }

    @Override
    protected void actualizarProgreso(Usuario usuario, UsuarioMision usuarioMision) {
        if (tieneMasDeDosHabilidadesUsadasEnAlgunaPartida(usuario)) {
            usuarioMision.setProgreso(usuarioMision.getProgreso() + 1);
            usuarioMision.setCompletada(Boolean.TRUE);
        }
    }
}
