package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
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

        servicioUsuario.regenerarVidasSiCorresponde(jugador);
        jugador = servicioUsuario.buscarUsuarioPorId(jugador.getId());

        if(jugador.getVidas() <= 0){
            request.getSession().setAttribute("mensajeVidas", "No tenés vidas disponibles! Esperá a que se recarguen...");
            request.getSession().setAttribute("mostrarPopupVidas", true);
            return new ModelAndView("redirect:/home");
        }

        if (jugador.getVidas() == 5) {
            jugador.setUltimaRegeneracionVida(LocalDateTime.now());
        }
        jugador.setVidas(jugador.getVidas() - 1);
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
         modelo.put("avatarImg", servicioUsuario.buscarUsuarioPorId(jugador.getId()).getAvatarActual().getLink());

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
            List<Respuesta>respuestasParaVista = new ArrayList<>(pregunta.getRespuestas());
            modelo.put("respuestasVista", respuestasParaVista);
            Boolean volviendoDeTrampa = (Boolean) session.getAttribute("volviendoDeTrampa");
            if (volviendoDeTrampa == null || !volviendoDeTrampa) {
                modelo.put("nuevaPregunta", true);
            }
            session.removeAttribute("volviendoDeTrampa");

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
        Boolean volviendoDeTrampa = (Boolean) session.getAttribute("volviendoDeTrampa");
        Pregunta p;

        if (Boolean.TRUE.equals(volviendoDeTrampa)) {
            p = (Pregunta) session.getAttribute("preguntaActual");
        } else {
            p = servicioPartida.obtenerPregunta(catEnum, idUsuario);
            session.setAttribute("preguntaActual", p);
        }

        if (p == null) {
            modelo.put("error", "No se pudo obtener una pregunta para la categoría seleccionada.");
            return new ModelAndView("errorVistaPregunta", modelo); // o redirigí a una página de error
        }

        List<Respuesta> respuestasParaVista;

        Long trampaActiva = (Long) session.getAttribute("trampaActiva");

        if (trampaActiva != null) {
            List<Respuesta> originales = new ArrayList<>(p.getRespuestas());
            List<Respuesta> incorrectas = originales.stream()
                    .filter(r -> !r.getOpcionCorrecta())
                    .collect(Collectors.toList());

            respuestasParaVista = new ArrayList<>(originales); // default

            switch (trampaActiva.intValue()) {
                case 1: // Elimina 1 respuesta incorrecta
                    Collections.shuffle(incorrectas);
                    if (!incorrectas.isEmpty()) {
                        respuestasParaVista.remove(incorrectas.get(0));
                    }
                    break;
                case 2: // Elimina 2 respuestas incorrectas
                    Collections.shuffle(incorrectas);
                    for (int i = 0; i < Math.min(2, incorrectas.size()); i++) {
                        respuestasParaVista.remove(incorrectas.get(i));
                    }
                    break;
                case 3: // Elimina 3 respuestas incorrectas
                    Collections.shuffle(incorrectas);
                    for (int i = 0; i < Math.min(3, incorrectas.size()); i++) {
                        respuestasParaVista.remove(incorrectas.get(i));
                    }
                    break;
                case 4: // Agrega 5 segundos
                    modelo.put("tiempoExtra", 5);
                    break;
                case 5: // Agrega 10 segundos
                    modelo.put("tiempoExtra", 10);
                    break;
                default:
                    break;
            }

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

        if (volviendoDeTrampa == null || !volviendoDeTrampa) {
            modelo.put("nuevaPregunta", true);
        }
        session.removeAttribute("volviendoDeTrampa");

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

        List<Respuesta>respuestasParaVista = new ArrayList<>(actual.getRespuestas());
        modelo.put("respuestasVista", respuestasParaVista);

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
            @RequestParam("idUsuario") Long idUsuario,HttpServletRequest request) throws UsuarioNoExistente {
        ModelMap modelo = new ModelMap();

        HttpSession session = request.getSession();
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
            siguiente = servicioPartida.validarRespuesta(resultadoRespuesta, modoJuego, request);
        }

        if (siguiente != null) {
            // Avanzamos a la siguiente pregunta
            modelo.put("pregunta", siguiente.getPregunta());
            modelo.put("categoria", siguiente.getPregunta().getTipoPregunta().name());
            modelo.put("respondida", false);
            modelo.put("orden", siguiente.getOrden());
            List<Respuesta>respuestasParaVista = new ArrayList<>(siguiente.getPregunta().getRespuestas());
            modelo.put("respuestasVista", respuestasParaVista);
        } else if (servicioPartida.partidaTerminada(idPartida)) {
            // Partida finalizada
            Partida partida = servicioPartida.buscarPartidaPorId(idPartida);

            if (partida.getGanador() != null && partida.getGanador().getId().equals(usuario.getId())) {
                session.setAttribute("xpAcumuladoTurno", 150);
                modelo.put("ganador", true);
            }else {
                modelo.put("ganador", false);
            }

            session.setAttribute("xpGanadoUltimoTurno", session.getAttribute("xpAcumuladoTurno"));
            session.removeAttribute("xpAcumuladoTurno");

            modelo.put("terminoPartida", true);
            modelo.put("mensajeFinal", "¡La partida ha finalizado!");
            modelo.put("mostrarVolver", true);
            modelo.put("pregunta", resultadoRespuesta.getPregunta());
            List<Respuesta>respuestasParaVista = new ArrayList<>(resultadoRespuesta.getPregunta().getRespuestas());
            modelo.put("respuestasVista", respuestasParaVista);
            modelo.put("categoria", resultadoRespuesta.getPregunta().getTipoPregunta().name());
            modelo.put("orden", resultadoRespuesta.getOrden());
            modelo.put("respondida", true);
            modelo.put("idRespuestaSeleccionada",
                    resultadoRespuesta.getRespuestaSeleccionada() != null
                            ? resultadoRespuesta.getRespuestaSeleccionada().getId()
                            : -1L);
            modelo.put("respuestaCorrecta",
                    resultadoRespuesta.getPregunta().getRespuestas().stream()
                            .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                            .findFirst()
                            .orElse(null));
        } else {
            // Mostrar la misma pregunta hasta que el otro jugador responda
            modelo.put("pregunta", resultadoRespuesta.getPregunta());
            List<Respuesta>respuestasParaVista = new ArrayList<>(resultadoRespuesta.getPregunta().getRespuestas());
            modelo.put("respuestasVista", respuestasParaVista);
            modelo.put("categoria", resultadoRespuesta.getPregunta().getTipoPregunta().name());
            modelo.put("orden", resultadoRespuesta.getOrden());
            modelo.put("respondida", true);
            modelo.put("idRespuestaSeleccionada",
                    resultadoRespuesta.getRespuestaSeleccionada() != null
                            ? resultadoRespuesta.getRespuestaSeleccionada().getId()
                            : -1L);
            modelo.put("respuestaCorrecta",
                    resultadoRespuesta.getPregunta().getRespuestas().stream()
                            .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                            .findFirst()
                            .orElse(null));
            modelo.put("mensaje", "Esperando a que el otro jugador responda...");
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
                                     @RequestParam("idUsuario") Long idUsuario) throws UsuarioNoExistente {
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
        ResultadoRespuesta resultadoRespuesta = servicioPartida.validarRespuesta(resultado, modoJuego,request);

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