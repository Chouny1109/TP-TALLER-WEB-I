package com.tallerwebi.dominio.excepcion;

public class MisionNotFoundException extends RuntimeException {
    public MisionNotFoundException(String mision) {
        super(mision);
    }
}
