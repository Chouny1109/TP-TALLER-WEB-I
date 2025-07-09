package com.tallerwebi.model;

public class Mensaje {
    private String contenido;
    private String emisor;
    private String receptor;

    public Mensaje() {
    }

    public Mensaje(String contenido, String emisor, String receptor) {
        this.contenido = contenido;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }
}
