package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;



public interface RepositorioPartida {

    Boolean guardarPartida(Partida partida);
    void actualizarPartida(Partida partida);
    Partida obtenerPartidaAbiertaPorModo(TIPO_PARTIDA tipo);
}
