package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioPartida;
import com.tallerwebi.service.ServicioTrampaUsuario;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/partida")
public class PartidaController {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Autowired
    private ServicioPartida servicioPartida;
    private IServicioUsuario servicioUsuario;
    private final SimpMessagingTemplate messagingTemplate;
    private ServicioTrampaUsuario servicioTrampaUsuario;

    @Autowired
    public PartidaController(ServicioPartida servicioPartida, IServicioUsuario servicioUsuario, SimpMessagingTemplate messagingTemplate, ServicioTrampaUsuario servicioTrampaUsuario) {
        this.servicioPartida = servicioPartida;
        this.servicioUsuario = servicioUsuario;
        this.messagingTemplate = messagingTemplate;
        this.servicioTrampaUsuario = servicioTrampaUsuario;
    }


    @GetMapping("/cargar")
    public ModelAndView cargarPartida(HttpServletRequest request,
                                      @RequestParam("modoJuego") TIPO_PARTIDA modoJuego) {

        ModelMap modelo = new ModelMap();

        Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
        if (jugador == null) {
            return new ModelAndView("redirect:/login");
        }

        if(jugador.getVidas() <= 0){
            request.getSession().setAttribute("mensajeVidas", "No tenés vidas disponibles! Esperá a que se recarguen...");
            request.getSession().setAttribute("mostrarPopupVidas", true);
            return new ModelAndView("redirect:/home");
        }

        jugador.setVidas(jugador.getVidas() - 1);
        jugador.setUltimaRegeneracionVida(LocalDateTime.now());
        servicioUsuario.actualizar(jugador);
        request.getSession().setAttribute("USUARIO", jugador);

        modelo.put("jugador", jugador);
        modelo.put("idUsuario", jugador.getId());
        modelo.put("modoJuego", modoJuego);

        List<Partida> p = servicioPartida.obtenerPartidasAbiertasConTurnoEnNull(modoJuego, jugador);

        if (p.isEmpty() && modoJuego.equals(TIPO_PARTIDA.MULTIJUGADOR)) {
            Partida partida = servicioPartida.crearOUnirsePartida(jugador, modoJuego);
            modelo.put("partida", partida);
            modelo.put("idPartida", partida.getId());
            return new ModelAndView("ruletaCategoria", modelo);
        }

        Partida partida = servicioPartida.crearOUnirsePartida(jugador, modoJuego);
        modelo.put("partida", partida);
        modelo.put("idPartida", partida.getId());
        String avatarImg = this.servicioUsuario.obtenerImagenAvatarSeleccionado(jugador.getId());
        modelo.put("avatarImg", avatarImg);

        if (modoJuego.equals(TIPO_PARTIDA.SUPERVIVENCIA)) {
            scheduler.schedule(() -> {
                List<Usuario> jugadores = servicioPartida.obtenerJugadoresEnPartida(partida.getId());
                if (jugadores.size() <= 1) {
                    finalizarPartida(partida.getId());
                } else {
                    System.out.println("No se finaliza la partida " + partida.getId() + " porque ya tiene rival.");
                }
            }, 20, TimeUnit.SECONDS);

        }
        return new ModelAndView("cargarPartida", modelo);
    }


    public void finalizarPartida(Long idPartida) {
        servicioPartida.finalizarPartida(idPartida);
    }

    @MessageMapping("/crearOUnirsePartida")
    public void crearOUnirsePartidaWS(PartidaRequest request) {
        Usuario jugador = servicioUsuario.buscarUsuarioPorId(request.getUsuarioId());
        servicioPartida.crearOUnirsePartida(jugador, request.getModoJuego());
    }

    @GetMapping("/ruletaCategoria")
    public ModelAndView ruletaCategoria(@RequestParam("id") Long idPartida,
                                        @RequestParam("idUsuario") Long idUsuario,
                                        @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
                                        HttpServletRequest request) {

        ModelMap modelo = new ModelMap();
        modelo.put("idPartida", idPartida);
        modelo.put("idUsuario", idUsuario);

        // si querés también:
        Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
        modelo.put("jugador", jugador);
        modelo.put("modoJuego", modoJuego);


        // Obtener la entidad que tiene las categorías ganadas
        Partida partida = servicioPartida.buscarPartidaPorId(idPartida);
        Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);

        CategoriasGanadasEnPartida categoriasGanadas = servicioPartida.obtenerCategoriasGanadasDeUsuarioEnPartida(partida, usuario);

        Set<CATEGORIA_PREGUNTA> categorias = categoriasGanadas != null ? categoriasGanadas.getCategoriasGanadas() : Collections.emptySet();

        modelo.put("categoriasGanadas", categorias);

        return new ModelAndView("ruletaCategoria", modelo);
    }


    @RequestMapping(value = "/pregunta", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView mostrarPregunta(@RequestParam("categoria") String categoria,
                                        @RequestParam("id") Long idPartida,
                                        @RequestParam("idUsuario") Long idUsuario,
                                        @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
                                        HttpServletRequest request) {

        Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
        if (jugador == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap modelo = new ModelMap();
        modelo.put("jugador", jugador);
        modelo.put("categoria", categoria);
        modelo.put("idPartida", idPartida);
        modelo.put("idUsuario", idUsuario);
        modelo.put("modoJuego", modoJuego);

        HttpSession session = request.getSession();
        Partida partida = servicioPartida.buscarPartidaPorId(idPartida);
        Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);

        CategoriasGanadasEnPartida categoriasGanadas = servicioPartida.obtenerCategoriasGanadasDeUsuarioEnPartida(partida, usuario);
        // Modo CORONA
        if ("CORONA".equalsIgnoreCase(categoria)) {
            session.setAttribute("modoCorona", true);

            Set<CATEGORIA_PREGUNTA> categorias = categoriasGanadas != null ? categoriasGanadas.getCategoriasGanadas() : Collections.emptySet();
            List<String> nombresCategorias = categorias.stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            modelo.put("categoriasGanadas", nombresCategorias);
            return new ModelAndView("elegirCategoria", modelo);
        }

        // Modo SUPERVIVENCIA / GENERAL
        if ("GENERAL".equalsIgnoreCase(categoria)) {
            Pregunta pregunta = servicioPartida.obtenerPreguntaSupervivencia(idPartida, jugador, null).getSiguientePregunta();

            modelo.put("pregunta", pregunta);
            modelo.put("respondida", false);
            return new ModelAndView("preguntas", modelo);
        }



        Set<CATEGORIA_PREGUNTA> categorias = categoriasGanadas != null ? categoriasGanadas.getCategoriasGanadas() : Collections.emptySet();
        List<String> nombresCategorias = categorias.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        modelo.put("categoriasGanadas", nombresCategorias);

        // Validar que la categoría sea una del enum
        boolean valida = Arrays.stream(CATEGORIA_PREGUNTA.values())
                .anyMatch(c -> c.name().equalsIgnoreCase(categoria));
        if (!valida) {
            return new ModelAndView("redirect:/errorCategoria");
        }

        // Si la pregunta viene desde el modo CORONA, guardamos la categoría elegida
        Boolean esCorona = (Boolean) session.getAttribute("modoCorona");
        if (Boolean.TRUE.equals(esCorona)) {
            CATEGORIA_PREGUNTA catElegida = CATEGORIA_PREGUNTA.valueOf(categoria.toUpperCase());
            session.setAttribute("categoriaCorona", catElegida);
        }

        // Pregunta normal (o desde corona pero ya con categoría)
        CATEGORIA_PREGUNTA catEnum = CATEGORIA_PREGUNTA.valueOf(categoria.toUpperCase());
        Pregunta p = servicioPartida.obtenerPregunta(catEnum, idUsuario);

        if (p == null) {
            modelo.put("error", "No se pudo obtener una pregunta para la categoría seleccionada.");
            return new ModelAndView("errorVistaPregunta", modelo); // o redirigí a una página de error
        }

        List<Respuesta> respuestasParaVista;

        Long trampaActiva = (Long) session.getAttribute("trampaActiva");
        if (trampaActiva != null && trampaActiva == 1L) {
            List<Respuesta> originales = new ArrayList<>(p.getRespuestas());
            List<Respuesta> incorrectas = originales.stream()
                    .filter(r -> !r.getOpcionCorrecta())
                    .collect(Collectors.toList());

            Collections.shuffle(incorrectas);
            respuestasParaVista = new ArrayList<>();
            respuestasParaVista.addAll(incorrectas.subList(0, Math.min(2, incorrectas.size())));

            originales.stream()
                    .filter(Respuesta::getOpcionCorrecta)
                    .findFirst()
                    .ifPresent(respuestasParaVista::add);

            Collections.shuffle(respuestasParaVista);
            session.removeAttribute("trampaActiva");
        } else {
            respuestasParaVista = new ArrayList<>(p.getRespuestas());
        }


        modelo.put("respuestasVista", respuestasParaVista);
        modelo.put("pregunta", p);
        modelo.put("respondida", false);

        List<TrampaUsuario> trampasJugador = servicioTrampaUsuario.obtenerTrampasDelUsuario(idUsuario);
        modelo.put("trampas", trampasJugador);
        session.setAttribute("preguntaActual", p);

        return new ModelAndView("preguntas", modelo);
    }


    @PostMapping("/validar-respuesta")
    public ModelAndView validarRespuesta(
            @RequestParam("respuesta") Long idrespuestaSeleccionada,
            @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("idPreguntaRespondida") Long idPreguntaRespondida,
            HttpServletRequest request) {

        Pregunta actual = servicioPartida.buscarPreguntaPorId(idPreguntaRespondida);
        Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);


        // Preparar datos para la vista
        Respuesta respuestaSeleccionada;
        if (idrespuestaSeleccionada != -1) {
            respuestaSeleccionada = servicioPartida.buscarRespuestaPorId(idrespuestaSeleccionada);
        } else {
            // Crear respuesta dummy si no respondió
            respuestaSeleccionada = new Respuesta();
            respuestaSeleccionada.setId(idrespuestaSeleccionada);
            respuestaSeleccionada.setDescripcion("Sin responder");

        }

        ModelMap modelo = new ModelMap();
        modelo.put("pregunta", actual);

        if(modoJuego.equals(TIPO_PARTIDA.MULTIJUGADOR)){

            CategoriasGanadasEnPartida categoriasGanadas = servicioPartida.obtenerCategoriasGanadasDeUsuarioEnPartida(servicioPartida.buscarPartidaPorId(idPartida), usuario);

            Set<CATEGORIA_PREGUNTA> categorias = categoriasGanadas != null ? categoriasGanadas.getCategoriasGanadas() : Collections.emptySet();
            List<String> nombresCategorias = categorias.stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());

            modelo.put("categoriasGanadas", nombresCategorias);
        }



        modelo.put("idRespuestaSeleccionada", respuestaSeleccionada.getId());
        modelo.put("respuestaCorrecta", actual != null ? actual.getRespuestas().stream()
                .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                .findFirst()
                .orElse(null) : null);
        modelo.put("modoJuego", modoJuego);
        modelo.put("idUsuario", idUsuario);
        modelo.put("idPartida", idPartida);
        modelo.put("respondida", true);

        return new ModelAndView("preguntas", modelo);
    }

    @PostMapping("/validar-rival")
    public ModelAndView validarRespuestaRival(
            @RequestParam("idRespuestaSeleccionada") Long idRespuestaSeleccionada,
            @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("preguntaRespondida") Long preguntaRespondida,
            @RequestParam("idUsuario") Long idUsuario) {

        ModelMap modelo = new ModelMap();

        Pregunta preguntaResp = servicioPartida.buscarPreguntaPorId(preguntaRespondida);
        Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);

        Respuesta respuestaSeleccionada = (idRespuestaSeleccionada != -1)
                ? servicioPartida.buscarRespuestaPorId(idRespuestaSeleccionada)
                : null;
        Boolean tiempoTerminadoRespuestaNula = (idRespuestaSeleccionada == -1);

        ResultadoRespuesta resultadoRespuesta = servicioPartida.obtenerResultadoPorPartidaUsuarioYPregunta(idPartida, usuario, preguntaResp);

        if (resultadoRespuesta == null) {
            resultadoRespuesta = servicioPartida.crearResultadoRespuestaConSiguienteOrden(
                    preguntaRespondida, idPartida, usuario, idRespuestaSeleccionada
            );
        } else if (resultadoRespuesta.getRespuestaSeleccionada() == null) {
            resultadoRespuesta.setRespuestaSeleccionada(respuestaSeleccionada);
            resultadoRespuesta.setTiempoTerminadoRespuestaNula(tiempoTerminadoRespuestaNula);
            servicioPartida.actualizarResultadoRespuesta(resultadoRespuesta);
        }

        boolean ambosRespondieron = servicioPartida.chequearAmbosRespondieron(idPartida, usuario, resultadoRespuesta.getOrden());

        ResultadoRespuesta siguiente = null;
        if (ambosRespondieron) {
            siguiente = servicioPartida.validarRespuesta(resultadoRespuesta, modoJuego);
        }

        boolean terminoPartida = false;
        if (modoJuego == TIPO_PARTIDA.SUPERVIVENCIA && siguiente == null) {
            int ordenSiguiente = resultadoRespuesta.getOrden() + 1;
            SiguientePreguntaSupervivencia siguienteGenerada = servicioPartida.obtenerSiguientePreguntaEntidad(idPartida, ordenSiguiente);
            terminoPartida = (siguienteGenerada == null); // SOLO termina si no hay siguiente
        }

        if (siguiente != null) {
            // Hay siguiente pregunta, mostrarla
            modelo.put("pregunta", siguiente.getPregunta());
            modelo.put("categoria", siguiente.getPregunta().getTipoPregunta().name());
            modelo.put("respondida", false);
        } else if (!terminoPartida) {
            // Aún no terminó, pero esperando al rival o la siguiente ya fue generada
            int ordenSiguiente = resultadoRespuesta.getOrden() + 1;
            SiguientePreguntaSupervivencia siguienteGenerada = servicioPartida.obtenerSiguientePreguntaEntidad(idPartida, ordenSiguiente);

            if (siguienteGenerada != null) {
                Pregunta pregunta = siguienteGenerada.getSiguientePregunta();
                modelo.put("pregunta", pregunta);
                modelo.put("categoria", pregunta.getTipoPregunta().name());
                modelo.put("respondida", false);
            } else {
                // Todavía no se generó la siguiente porque falta el rival
                modelo.put("pregunta", resultadoRespuesta.getPregunta());
                modelo.put("respondida", true);
                modelo.put("mensajeFinal", "Esperando a tu rival...");


                modelo.put("idRespuestaSeleccionada",
                        resultadoRespuesta.getRespuestaSeleccionada() != null ? resultadoRespuesta.getRespuestaSeleccionada().getId() : -1L);
                modelo.put("respuestaCorrecta",
                        resultadoRespuesta.getPregunta().getRespuestas().stream()
                                .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                                .findFirst()
                                .orElse(null));
            }
        } else {
            // Terminó la partida
            modelo.put("pregunta", resultadoRespuesta.getPregunta());
            modelo.put("orden", resultadoRespuesta.getOrden());

            if(servicioPartida.partidaTerminada(idPartida)) {
                modelo.put("terminoPartida", true);
                modelo.put("mensajeFinal", "¡La partida ha finalizado!");
                modelo.put("mostrarVolver", true);
            }

            modelo.put("respondida", true);


            modelo.put("idRespuestaSeleccionada",
                    resultadoRespuesta.getRespuestaSeleccionada() != null ? resultadoRespuesta.getRespuestaSeleccionada().getId() : -1L);
            modelo.put("respuestaCorrecta",
                    resultadoRespuesta.getPregunta().getRespuestas().stream()
                            .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                            .findFirst()
                            .orElse(null));
        }

        modelo.put("modoJuego", modoJuego);
        modelo.put("idUsuario", idUsuario);
        modelo.put("idPartida", idPartida);

        return new ModelAndView("preguntas", modelo);
    }

    @PostMapping("validar-turno")
    public ModelAndView validarTurno(HttpServletRequest request,
                                     @RequestParam("idRespuestaSeleccionada") Long idRespuestaSeleccionada,
                                     @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
                                     @RequestParam("idPartida") Long idPartida,
                                     @RequestParam("preguntaRespondida") Long preguntaRespondida,
                                     @RequestParam("idUsuario") Long idUsuario) {
        ModelMap modelo = new ModelMap();
        Pregunta preguntaResp = servicioPartida.buscarPreguntaPorId(preguntaRespondida);
        Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);

        Respuesta respuestaSeleccionada = (idRespuestaSeleccionada != -1)
                ? servicioPartida.buscarRespuestaPorId(idRespuestaSeleccionada)
                : null;
        Boolean tiempoTerminadoRespuestaNula = (idRespuestaSeleccionada == -1);

        HttpSession session = request.getSession();
        Boolean esCorona = (Boolean) session.getAttribute("modoCorona");
        CATEGORIA_PREGUNTA categoriaCorona = (CATEGORIA_PREGUNTA) session.getAttribute("categoriaCorona");

        ResultadoRespuesta resultado = servicioPartida.crearResultadoRespuestaParaMultijugador(idPartida, usuario, preguntaRespondida, idRespuestaSeleccionada);
        ResultadoRespuesta resultadoRespuesta = servicioPartida.validarRespuesta(resultado, modoJuego);

        Integer xpAcumuladoTurno = (Integer) session.getAttribute("xpAcumuladoTurno");
        if (xpAcumuladoTurno == null) xpAcumuladoTurno = 0;


        if (resultadoRespuesta != null) {
            int xpGanado = resultadoRespuesta.getXpEnTurno() != null ? resultadoRespuesta.getXpEnTurno() : 0;
            xpAcumuladoTurno += xpGanado;
            session.setAttribute("xpAcumuladoTurno", xpAcumuladoTurno);


            modelo.put("idPartida", idPartida);
            modelo.put("idUsuario", idUsuario);

            Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
            modelo.put("jugador", jugador);
            modelo.put("modoJuego", modoJuego);

            if (Boolean.TRUE.equals(esCorona) && categoriaCorona != null) {
                servicioPartida.agregarCategoriaGanadaEnPartida(idPartida, usuario, categoriaCorona);
            }
            //  Limpieza después de que se usó la lógica de corona
            session.removeAttribute("modoCorona");
            session.removeAttribute("categoriaCorona");
            return new ModelAndView("ruletaCategoria", modelo);
        }

        // Cuando resultadoRespuesta es null (o no hay siguiente pregunta)

        session.setAttribute("xpGanadoUltimoTurno", xpAcumuladoTurno);
        session.removeAttribute("xpAcumuladoTurno");

        modelo.put("pregunta", preguntaResp);
        modelo.put("respondida", true);

        modelo.put("idRespuestaSeleccionada",
                respuestaSeleccionada != null ? respuestaSeleccionada.getId() : -1L);
        modelo.put("respuestaCorrecta",
                preguntaResp.getRespuestas().stream()
                        .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                        .findFirst()
                        .orElse(null));

        modelo.put("modoJuego", modoJuego);
        modelo.put("idUsuario", idUsuario);
        modelo.put("idPartida", idPartida);
        modelo.put("mostrarVolver", true);

        return new ModelAndView("preguntas", modelo);
    }


}