package com.tallerwebi.model;

public class ResultadoRegistro {
    public Boolean exitoso = null;
    public Exception error = null;


    public Exception getError() {
        return error;
    }

    public Boolean getExitoso() {
        return exitoso;
    }

    public void setExitoso(Boolean exitoso) {
        this.exitoso = exitoso;
    }
    // Método que indica si fue exitoso
    public Boolean fueExitoso() {
      return this.exitoso ;
    }


    // Método que indica si hubo error
    public Boolean huboError() {
        return error != null;
    }

    // Verifica si el error fue de un tipo específico (como PasswordsNotEquals)
    public <T extends Exception> boolean huboErrorDeTipo(Class<T> tipo) {
        return tipo.isInstance(error);
    }

    // Devuelve la excepción como el tipo que vos quieras (por ejemplo, PasswordsNotEquals)
    public <T extends Exception> T getErrorComo(Class<T> tipo) {
        return tipo.cast(error);
    }
}
