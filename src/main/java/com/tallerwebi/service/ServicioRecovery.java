package com.tallerwebi.service;

import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.TokenInvalido;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.DatosRecovery;
import com.tallerwebi.model.RecoveryToken;
import com.tallerwebi.model.Usuario;

public interface ServicioRecovery {
    Boolean enviarEmailDeRecuperacion(String email) throws UsuarioNoExistente;
    Usuario cambiarPassword(DatosRecovery datosRecovery, String token) throws PasswordsNotEquals, UsuarioNoExistente, EmailInvalido, TokenInvalido;
    RecoveryToken obtenerToken(String token);

}
