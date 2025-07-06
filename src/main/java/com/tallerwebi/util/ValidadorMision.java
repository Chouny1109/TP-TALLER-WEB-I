package com.tallerwebi.util;

import com.tallerwebi.dominio.excepcion.MisionNoPerteneceAlUsuarioException;
import com.tallerwebi.dominio.excepcion.MisionNotFoundException;
import com.tallerwebi.dominio.excepcion.MisionYaFinalizadaException;
import com.tallerwebi.dominio.excepcion.MonedasInsuficientesException;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMision {
    public static final int COSTO_MISION = 800;

    public void validarMision(Usuario usuario, UsuarioMision usuarioMision) {

        if (usuarioMision == null) {
            throw new MisionNotFoundException("No se ha encontrado la relacion");
        }

        if (!usuario.getId().equals(usuarioMision.getUsuario().getId())) {
            throw new MisionNoPerteneceAlUsuarioException("La mision no le pertenece al usuario");
        }

        if (usuarioMision.getCanjeada() || usuarioMision.getCompletada()) {
            throw new MisionYaFinalizadaException("La mision ya fue completada/canjeada");
        }

        if (usuario.getMonedas() < COSTO_MISION) {
            throw new MonedasInsuficientesException("El usuario no cuenta con monedas suficientes");
        }
    }
}
