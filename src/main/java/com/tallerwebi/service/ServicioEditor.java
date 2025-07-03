package com.tallerwebi.service;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Respuesta;

import java.util.List;

public interface ServicioEditor {
    List<Pregunta> ObtenerTodasLasPreguntas();
    List<Pregunta> ObtenerPreguntasPorCategoria(CATEGORIA_PREGUNTA categoria);
    void editarPreguntaYRespuestas(Long idPregunta, String enunciado, String[] ids, String[] textos, Long idRespuestaCorrecta);
    void cambiarEstadoPregunta(Long idPregunta);
    List<String> obtenerCategorias();
    List<Pregunta> obtenerPreguntasPorCategoria(String categoria);
    void agregarPreguntasYRespuestas(String categoriaPregunta, String enunciado, String[] textos, Long idRespuestaCorrecta);
    void eliminarPregunta(Long idPregunta);
}
