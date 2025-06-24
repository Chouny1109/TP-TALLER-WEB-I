package com.tallerwebi.service;

import com.tallerwebi.model.Usuario;

public interface IServicioUsuario {
    String obtenerImagenAvatarSeleccionado(Long id);
    Usuario buscarUsuarioPorId(Long idUsuario);
    void agregarMonedas(Long idUsuario, int cantidad);
    boolean descontarMonedas(Long idUsuario, int cantidad);
}