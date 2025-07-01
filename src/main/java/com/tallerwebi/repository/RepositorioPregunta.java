package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Respuesta;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface RepositorioPregunta {
    Pregunta buscarPreguntaPorId();
    Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario);
    List<Pregunta> listasPreguntasRandomParaPartida(Long idPregunta);
    Pregunta buscarPreguntaPorId(Long idPregunta);
    Respuesta buscarRespuestaPorId(Long idRespuesta);
    Pregunta obtenerPreguntaSupervivencia(List<Usuario> jugadores);
    void actualizar(Pregunta pregunta);
    void actualizarRespuestas(Respuesta respuesta);
    void eliminarPregunta(Long idPregunta);
    void agregarNuevaPregunta(Pregunta pregunta);
    List<Pregunta> obtenerPreguntasPorCategoria(String categoria);
}
