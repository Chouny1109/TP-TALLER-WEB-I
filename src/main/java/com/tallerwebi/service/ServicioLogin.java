package com.tallerwebi.service;

import com.tallerwebi.model.Usuario;

import javax.servlet.http.HttpServletRequest;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void cerrarSesion(HttpServletRequest request);
}
