package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;

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

    Partida buscarPartidaPorId(Long idPartida);

    ResultadoRespuesta obtenerResultadoRespuestaEnPartidaPorJugador(Long idPartida, Usuario jugador);

    List<ResultadoRespuesta> obtenerResultadoRespuestaDeJugadoresEnPartida(Long idPartida);

    void guardarResultadoRespuesta(ResultadoRespuesta resultadoRespuesta);

    void actualizarResultadoRespuesta(ResultadoRespuesta resultadoRespuesta);

    boolean existeUsuarioRespondePregunta(Long idUsuario, Long idPregunta);

    void guardarUsuarioRespondePregunta(UsuarioRespondePregunta usp);

    ResultadoRespuesta obtenerUltimoResultadoRespuestaEnPartidaPorJugador(Long idPartida, Usuario jugador);

    ResultadoRespuesta obtenerUltimoResultadoRespuestaEnPartidaDeRival(Long idPartida, Usuario jugador);

    SiguientePreguntaSupervivencia obtenerSiguientePreguntaEntidad(Partida partida, Integer orden);

    void guardarSiguientePregunta(SiguientePreguntaSupervivencia siguientePregunta);

    Integer obtenerMaxOrdenSiguientePregunta(Long idPartida);

    ResultadoRespuesta obtenerResultadoPorOrdenYPregunta(Long idPartida, Usuario usuario, int nuevoOrden, Pregunta pregunta);

    ResultadoRespuesta obtenerResultadoPorPartidaUsuarioYPregunta(Long idPartida, Usuario usuario, Pregunta preguntaResp);

}
