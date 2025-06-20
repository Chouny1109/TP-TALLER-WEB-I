package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioPartida;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface RepositorioPartida {

    Boolean guardarPartida(Partida partida);

    void actualizarPartida(Partida partida);

    List<Partida> obtenerPartidasAbiertaPorModo(TIPO_PARTIDA tipo);

    void agregarUsuarioPartidaRelacion(UsuarioPartida usuarioPartida);

    Usuario obtenerRivalDePartida(Long idPartida, Long id);

    List<Usuario> obtenerJugadoresDePartida(Long idPartida);

    Boolean jugadorEstaJugando(Long idJugador);

    List<UsuarioPartida>obtenerLasPartidasDelUsuarioParaDeterminadaFecha(Long id, LocalDateTime fecha);

    void finalizarPartida(Long idPartida);

    Partida obtenerPartidaActivaDeJugador(Long idJugador);

    Integer obtenerCantidadDePartidasJugadasParaLaFecha(Long id, LocalDate fecha);
}
