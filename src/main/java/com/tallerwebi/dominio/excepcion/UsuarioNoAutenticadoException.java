package com.tallerwebi.dominio.excepcion;

public class UsuarioNoAutenticadoException extends RuntimeException {
    public UsuarioNoAutenticadoException(String mision) {
        super(mision);
    }

}
