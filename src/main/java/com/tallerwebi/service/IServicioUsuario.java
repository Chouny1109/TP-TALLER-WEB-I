package com.tallerwebi.service;

import com.tallerwebi.dominio.enums.ROL_USUARIO;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface IServicioUsuario {
    String obtenerImagenAvatarSeleccionado(Long id);
    Usuario buscarUsuarioPorId(Long idUsuario);
    void agregarMonedas(Long idUsuario, int cantidad);
    boolean descontarMonedas(Long idUsuario, int cantidad);
    List<Usuario> obtenerAmigos(Long idUsuario);
    Usuario buscarPorNombreUsuario(String nombreUsuario);
    void eliminarAmigo(Long idUsuario, Long idAmigo);
    boolean tieneMonedasSuficientes(Long idUsuario, int costo);
    Long obtenerCantidadDeUsuarios();
    void banearUsuario(Long idUsuario);
    void desbanearUsuario(Long idUsuario);
    void asignarRol(Long idUsuario, ROL_USUARIO nuevoRol);
    List<Usuario> obtenerTodosLosUsuarios();
    boolean usuarioTieneAvatar(Long idUsuario, Long idAvatar);
    List<Long> obtenerIdsAvataresDelUsuario(Long idUsuario);
    void agregarVidas(Long idUsuario, int cantidad);
    void regenerarVidasSiCorresponde(Usuario usuario);
    void actualizar(Usuario usuario);
}