package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.TIPO_PREGUNTA;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Pregunta {

    private String enunciado;
    private List<String> opciones;
    private TIPO_PREGUNTA tipoPregunta;

    public Pregunta(String enunciado, List<String> opciones, TIPO_PREGUNTA tipoPregunta) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.tipoPregunta = tipoPregunta;
    }

    // Getters y setters
    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public TIPO_PREGUNTA getTipoPregunta(){
        return tipoPregunta;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pregunta)) return false;
        Pregunta that = (Pregunta) o;
        return Objects.equals(enunciado, that.enunciado) &&
                Objects.equals(opciones, that.opciones) &&
                tipoPregunta == that.tipoPregunta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enunciado, opciones, tipoPregunta);
    }
}
