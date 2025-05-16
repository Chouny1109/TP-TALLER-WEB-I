package com.tallerwebi.service;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.model.Usuario;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);

}
