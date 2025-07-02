package com.tallerwebi.service.impl;

import com.tallerwebi.model.Partida;
import com.tallerwebi.model.SiguientePreguntaSupervivencia;
import com.tallerwebi.repository.RepositorioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ServicioPartidaTransaccional {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RepositorioPartida repositorioPartida;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void guardarSiguientePregunta(SiguientePreguntaSupervivencia siguiente) {
        repositorioPartida.guardarSiguientePregunta(siguiente);
        em.flush();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SiguientePreguntaSupervivencia obtenerPreguntaExistenteConNuevaTransaccion(Partida partida, Integer ordenBuscado) {
        return repositorioPartida.obtenerSiguientePreguntaEntidad(partida, ordenBuscado);
    }
}
