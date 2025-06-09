package com.tallerwebi.service;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;

public interface ServicioPartida {

    Partida crearOUnirsePartida(Usuario jugador, TIPO_PARTIDA modoJuego);
    //metodos para la logica de la partida
}
