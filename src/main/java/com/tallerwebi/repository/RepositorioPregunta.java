package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Pregunta;

import java.util.List;

public interface RepositorioPregunta {
    Pregunta buscarPreguntaPorId();
    Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario);
    List<Pregunta> listasPreguntasRandomParaPartida();

}
