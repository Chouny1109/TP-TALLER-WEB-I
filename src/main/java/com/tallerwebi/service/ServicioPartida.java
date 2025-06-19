package com.tallerwebi.service;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface ServicioPartida {

    Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego);
    List<Usuario> obtenerJugadoresEnPartida(Long id);

    void finalizarPartida(Long idPartida);

    void obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario);
    //metodos para la logica de la partida
}
