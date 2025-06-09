package com.tallerwebi.repository;

import com.tallerwebi.model.Pregunta;

import java.util.List;

public interface RepositorioPregunta {
    Pregunta buscarPreguntaPorId();
    List<Pregunta> listasPreguntasRandomParaPartida();

}
