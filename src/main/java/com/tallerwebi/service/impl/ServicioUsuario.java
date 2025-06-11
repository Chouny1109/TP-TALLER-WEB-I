package com.tallerwebi.service.impl;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuario {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public String obtenerImagenAvatarSeleccionado(Long id) {
        return repositorioUsuario.obtenerImagenAvatarSeleccionado(id);
    }

    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return repositorioUsuario.buscarUsuarioPorId(usuarioId);
    }
}
