package com.tallerwebi.dominio.excepcion;

public class MisionYaFinalizadaException extends RuntimeException {
    public MisionYaFinalizadaException(String mision) {
        super(mision);
    }
}
