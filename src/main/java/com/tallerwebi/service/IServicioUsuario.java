package com.tallerwebi.service;

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
}