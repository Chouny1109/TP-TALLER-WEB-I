package com.tallerwebi.repository;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioAvatar;

import java.util.List;

public interface RepositorioUsuario {

    //    Usuario buscarUsuario(String email, String password);
    Boolean guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    Usuario buscarUsuarioPorId(Long id);
    String obtenerImagenAvatarSeleccionado(Long id);
    void asignarAvatarPorDefecto(UsuarioAvatar ua);
    Usuario obtenerUsuarioConPartidas(Long idUsuario);
    List<Usuario> obtenerUsuarios();
    List<Usuario> obtenerAmigos(Long idUsuario);
    Usuario buscarPorNombreUsuario(String nombreUsuario);
}