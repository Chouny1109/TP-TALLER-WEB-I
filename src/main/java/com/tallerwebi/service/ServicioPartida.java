package com.tallerwebi.service;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;

import javax.transaction.Transactional;
import java.util.List;

public interface ServicioPartida {

    Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego);

    List<Partida> obtenerPartidasAbiertasPorModo(TIPO_PARTIDA tipo);

    List<Usuario> obtenerJugadoresEnPartida(Long id);


    @Transactional
    SiguientePreguntaSupervivencia obtenerPreguntaSupervivencia(Long idPartida, Usuario usuario, ResultadoRespuesta resultadoUltimo);

    void finalizarPartida(Long idPartida);

    Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario);



    Respuesta buscarRespuestaPorId(Long idrespuestaSeleccionada);

    Partida buscarPartidaPorId(Long idPartida);

    @Transactional
    boolean partidaTerminada(Long idPartida);


    @org.springframework.transaction.annotation.Transactional
    ResultadoRespuesta crearResultadoRespuestaConOrdenFijo(Long idPregunta, Long idPartida, Usuario usuario, Long idRespuesta, Integer ordenFijo);

    @Transactional
    boolean chequearAmbosRespondieron(Long idPartida, Usuario jugador, Integer orden);

    @Transactional
    ResultadoRespuesta validarRespuesta(ResultadoRespuesta resultado, TIPO_PARTIDA modoJuego);

    void notificarEstadoPartida(Long idPartida, Usuario quienRespondio, boolean ambosRespondieron, boolean terminoPartida);
    //metodos para la logica de la partida

    Pregunta buscarPreguntaPorId(Long idPregunta);

    @Transactional
    ResultadoRespuesta crearResultadoRespuestaConSiguienteOrden(Long pregunta, Long partida, Usuario usuario, Long respuesta);

    @Transactional
    ResultadoRespuesta obtenerUltimoResultadoRespuestaDeJugador(Long idPartida, Usuario usuario);



    @Transactional
    void actualizarResultadoRespuesta(ResultadoRespuesta rr);

    ResultadoRespuesta obtenerResultadoPorPartidaUsuarioYPregunta(Long idPartida, Usuario usuario, Pregunta preguntaResp);
}
