package com.tallerwebi.service;


import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.model.Usuario;

import com.tallerwebi.model.DatosRegistro;
import com.tallerwebi.model.Usuario;


public interface ServicioRegistro {

    Boolean registrar(Usuario usuario, String confirmarPassword) throws UsuarioExistente, PasswordsNotEquals, EmailInvalido;

}
