package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;


import java.time.LocalDateTime;
import java.util.List;


public interface RepositorioPartida {

    List<Partida> obtenerPartidasAbiertasConTurnoEnNull(TIPO_PARTIDA modoJuego, Usuario jugador);

    Boolean guardarPartida(Partida partida);

    void actualizarPartida(Partida partida);

    List<Partida> obtenerPartidasAbiertaPorModo(TIPO_PARTIDA tipo);

    void agregarUsuarioPartidaRelacion(UsuarioPartida usuarioPartida);

    Usuario obtenerRivalDePartida(Long idPartida, Long id);

    List<Usuario> obtenerJugadoresDePartida(Long idPartida);

    Boolean jugadorEstaJugando(Long idJugador);

    List<UsuarioPartida> obtenerLasPartidasDelUsuarioParaDeterminadaFecha(Long id, LocalDateTime fecha);

    Integer obtenerCantidadDePartidasGanadasParaLaFecha(Long id, LocalDateTime fecha);

    void finalizarPartida(Long idPartida);

    Partida obtenerPartidaActivaDeJugador(Long idJugador);

    Integer obtenerCantidadDePartidasJugadasParaLaFecha(Long id, LocalDateTime fecha);

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

    CategoriasGanadasEnPartida obtenerCategoriasGanadasDeUsuarioEnPartida(Partida partida, Usuario usuario);

    void actualizarCategoriasGanadas(CategoriasGanadasEnPartida cat);

    void guardarCategoriasGanadas(CategoriasGanadasEnPartida cat);

    List<Partida> obtenerPartidasAbiertasOEnCursoMultijugadorDeUnJugador(Usuario u);

    ResultadoRespuesta obtenerResultadoPorOrden(Long p, Usuario jugador, Integer orden);

    ResultadoRespuesta obtenerResultadoRespuestaDeRivalPorOrden(Long idPartida, Usuario jugador, Integer orden);
}
