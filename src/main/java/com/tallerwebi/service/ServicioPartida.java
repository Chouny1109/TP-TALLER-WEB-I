package com.tallerwebi.service;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface ServicioPartida {

    Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego);
    List<Usuario> obtenerJugadoresEnPartida(Long id);
    //metodos para la logica de la partida
}
