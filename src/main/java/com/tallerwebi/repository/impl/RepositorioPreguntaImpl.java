package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Pregunta;
import com.tallerwebi.repository.RepositorioPregunta;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioPregunta")
@Transactional
public class RepositorioPreguntaImpl implements RepositorioPregunta {

    @Override
    public Pregunta buscarPreguntaPorId() {
        return null;
    }

    @Override
    public List<Pregunta> listasPreguntasRandomParaPartida() {
        return List.of();
    }
}
