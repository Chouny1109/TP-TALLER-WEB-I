package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioNivel;

public abstract class MisionPlantilla implements EstrategiaMision {

    private final RepositorioMisionUsuario repositorioMisionUsuario;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioNivel servicioNivel;

    public MisionPlantilla(RepositorioMisionUsuario repositorioMisionUsuario, RepositorioUsuario repositorioUsuario, ServicioNivel servicioNivel) {
        this.repositorioMisionUsuario = repositorioMisionUsuario;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioNivel = servicioNivel;
    }

    @Override
    public void completarMision(Usuario usuario, UsuarioMision usuarioMision) {
        if (usuario == null || usuarioMision == null) {
            return;
        }
        actualizarProgreso(usuario, usuarioMision);

        if (verificarCumplimiento(usuario, usuarioMision)) {
            otorgarRecompensa(usuario, usuarioMision);
        }

        guardarEnBd(usuario, usuarioMision);
    }

    protected abstract boolean verificarCumplimiento(Usuario usuario, UsuarioMision usuarioMision);

    protected abstract void actualizarProgreso(Usuario usuario, UsuarioMision usuarioMision);

    protected void otorgarRecompensa(Usuario usuario, UsuarioMision usuarioMision) {
        Mision mision = usuarioMision.getMision();
        servicioNivel.otorgarExperiencia(usuario, mision);

        Integer copasAnteriores = usuario.getCopas();
        usuario.setCopas(copasAnteriores + mision.getCopas());
    }

    protected void guardarEnBd(Usuario usuario, UsuarioMision usuarioMision) {
        this.repositorioMisionUsuario.save(usuarioMision);
        this.repositorioUsuario.modificar(usuario);
    }
}
