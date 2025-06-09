package com.tallerwebi.repository;

import com.tallerwebi.model.Pregunta;

import java.util.List;

public interface RepositorioPreguntaImpl {
    Pregunta buscarPreguntaPorId();
    List<Pregunta> listasPreguntasRandomParaPartida();

}
