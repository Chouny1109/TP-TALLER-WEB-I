package com.tallerwebi.service.impl;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.IServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioUsuario implements IServicioUsuario {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public String obtenerImagenAvatarSeleccionado(Long id) {
        return repositorioUsuario.obtenerImagenAvatarSeleccionado(id);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return repositorioUsuario.buscarUsuarioPorId(usuarioId);
    }

    @Override
    public void agregarMonedas(Long idUsuario, int cantidad) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        usuario.setMonedas(usuario.getMonedas() + cantidad);
        repositorioUsuario.modificar(usuario);
    }

    @Override
    public boolean tieneMonedasSuficientes(Long idUsuario, int costo) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        return usuario.getMonedas() >= costo;
    }

    @Override
    public boolean descontarMonedas(Long idUsuario, int cantidad) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if (usuario.getMonedas() >= cantidad) {
            usuario.setMonedas(usuario.getMonedas() - cantidad);
            repositorioUsuario.modificar(usuario);
            return true;
        }
        return false;
    }

    @Override
    public List<Usuario> obtenerAmigos(Long idUsuario) {
        return repositorioUsuario.obtenerAmigos(idUsuario);
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return repositorioUsuario.buscarPorNombreUsuario(nombreUsuario);
    }

    @Override
    public void eliminarAmigo(Long idUsuario, Long idAmigo) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        Usuario amigo = repositorioUsuario.buscarUsuarioPorId(idAmigo);

        if (usuario != null && amigo != null) {
            usuario.getAmigos().remove(amigo);
            amigo.getAmigos().remove(usuario);
            repositorioUsuario.modificar(usuario);
            repositorioUsuario.modificar(amigo);
        }
    }

}