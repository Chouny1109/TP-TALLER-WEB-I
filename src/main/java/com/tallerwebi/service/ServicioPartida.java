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

    void finalizarPartida(Long idPartida);

    Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario);



    Partida obtenerPreguntaSupervivencia(Long idPartida);

    Respuesta buscarRespuestaPorId(Long idrespuestaSeleccionada);

    Partida buscarPartidaPorId(Long idPartida);

    Pregunta validarRespuesta(Long idrespuestaSeleccionada, Long idPartida, TIPO_PARTIDA modoJuego, Usuario usuario);

    @Transactional
    boolean chequearAmbosRespondieron(Long idPartida, Usuario jugador);

    void notificarEstadoPartida(Long idPartida, Usuario quienRespondio, boolean ambosRespondieron, boolean terminoPartida);
    //metodos para la logica de la partida
}
