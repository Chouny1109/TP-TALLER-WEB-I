package com.tallerwebi.service.impl;

import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioPartida;
import com.tallerwebi.repository.RepositorioPregunta;
import com.tallerwebi.repository.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Service
public class ServicioPartidaTransaccional {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RepositorioPartida repositorioPartida;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioPregunta repositorioPregunta;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void guardarSiguientePregunta(SiguientePreguntaSupervivencia siguiente) {
        repositorioPartida.guardarSiguientePregunta(siguiente);
        em.flush();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SiguientePreguntaSupervivencia obtenerPreguntaExistenteConNuevaTransaccion(Partida partida, Integer ordenBuscado) {
        return repositorioPartida.obtenerSiguientePreguntaEntidad(partida, ordenBuscado);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultadoRespuesta guardarResultadoConFlush(ResultadoRespuesta resultado) {
        repositorioPartida.guardarResultadoRespuesta(resultado);
        em.flush();
        return resultado;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultadoRespuesta crearResultadoRespuestaConOrdenFijo(Long idPregunta, Long idPartida, Usuario usuario, Long idRespuesta, Integer ordenFijo) {
        Pregunta pregunta = repositorioPregunta.buscarPreguntaPorId(idPregunta);
        ResultadoRespuesta existente = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, usuario, ordenFijo, pregunta);

        if (existente != null) {
            return existente;
        }

        Partida partida = repositorioPartida.buscarPartidaPorId(idPartida);

        ResultadoRespuesta resultado = new ResultadoRespuesta();
        resultado.setPartida(partida);
        resultado.setPregunta(pregunta);
        resultado.setUsuario(usuario);
        resultado.setOrden(ordenFijo);

        Respuesta respuestaSeleccionada = null;
        if (idRespuesta == null) {
            resultado.setTiempoTerminadoRespuestaNula(Boolean.FALSE);
        } else if (idRespuesta == -1) {
            resultado.setTiempoTerminadoRespuestaNula(Boolean.TRUE);
        } else {
            respuestaSeleccionada = repositorioPregunta.buscarRespuestaPorId(idRespuesta);
            resultado.setTiempoTerminadoRespuestaNula(Boolean.FALSE);
        }

        resultado.setRespuestaSeleccionada(respuestaSeleccionada);

        resultado.setRespuestaCorrecta(
                pregunta.getRespuestas().stream()
                        .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                        .findFirst()
                        .orElse(null)
        );

        try {
            return guardarResultadoConFlush(resultado);
        } catch (PersistenceException e) {
            em.clear();
            System.out.println("⚠️ Error al guardar resultado fijo: " + e.getMessage());
            // Evitar rollback propagado: NO relanzar la excepción
            ResultadoRespuesta existenteRetry = repositorioPartida.obtenerResultadoPorOrdenYPregunta(idPartida, usuario, ordenFijo, pregunta);
            if (existenteRetry == null) {
                throw new IllegalStateException("No se pudo guardar ni recuperar el resultado fijo.");
            }
            return existenteRetry; }
    }

}
