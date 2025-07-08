package com.tallerwebi.dominio.excepcion;

public class MisionNoPerteneceAlUsuarioException extends RuntimeException {
    public MisionNoPerteneceAlUsuarioException(String mision) {
        super(mision);
    }
}
