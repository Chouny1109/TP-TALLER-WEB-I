package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioPartida;

import java.util.List;


public interface RepositorioPartida {

    Boolean guardarPartida(Partida partida);
    void actualizarPartida(Partida partida);
    Partida obtenerPartidaAbiertaPorModo(TIPO_PARTIDA tipo);
    void agregarUsuarioPartidaRelacion(UsuarioPartida usuarioPartida);
    Usuario obtenerRivalDePartida(Long idPartida, Long id);
    List<Usuario> obtenerJugadoresDePartida(Long idPartida);
}
