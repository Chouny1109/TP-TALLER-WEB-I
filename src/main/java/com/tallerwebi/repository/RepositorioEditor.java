package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Respuesta;

import java.util.List;

public interface RepositorioEditor {
    List<Pregunta> ObtenerTodasLasPreguntas();
    List<Pregunta> ObtenerPreguntasPorCategoria(CATEGORIA_PREGUNTA categoria);
    void ActualizarPregunta(String enunciado, List<Respuesta> Respuestas);
}
