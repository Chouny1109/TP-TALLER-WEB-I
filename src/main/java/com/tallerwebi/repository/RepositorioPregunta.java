package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Respuesta;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface RepositorioPregunta {

    Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario);
    List<Pregunta> listasPreguntasRandomParaPartida(Long idPregunta);

    Pregunta buscarPreguntaPorId(Long idPregunta);

    List<Pregunta> listasPreguntasRandomParaPartida();

    Respuesta buscarRespuestaPorId(Long idRespuesta);


    Pregunta obtenerPreguntaSupervivencia(List<Usuario> jugadores);
}
