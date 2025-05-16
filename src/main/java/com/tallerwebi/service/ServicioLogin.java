package com.tallerwebi.service;

import com.tallerwebi.model.Usuario;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);

}
