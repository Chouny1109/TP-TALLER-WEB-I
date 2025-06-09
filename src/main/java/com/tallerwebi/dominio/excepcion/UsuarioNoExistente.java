package com.tallerwebi.dominio.excepcion;

public class UsuarioNoExistente extends Exception {
    public UsuarioNoExistente() {
        super("El usuario no existe");
    }
}
