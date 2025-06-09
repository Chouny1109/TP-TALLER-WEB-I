package com.tallerwebi.model;

public class Amigo {
    private String nombre;
    private String imagen;

    public Amigo(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}
