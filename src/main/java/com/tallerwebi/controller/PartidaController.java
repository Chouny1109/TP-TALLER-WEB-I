package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioPartida;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/partida")
public class PartidaController {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
   @Autowired
    private ServicioPartida servicioPartida;
    private IServicioUsuario servicioUsuario;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PartidaController(ServicioPartida servicioPartida, IServicioUsuario servicioUsuario, SimpMessagingTemplate messagingTemplate) {
        this.servicioPartida = servicioPartida;
        this.servicioUsuario = servicioUsuario;
        this.messagingTemplate = messagingTemplate;
    }


    @GetMapping("/cargar")
    public ModelAndView cargarPartida(HttpServletRequest request, @RequestParam("modoJuego") TIPO_PARTIDA modoJuego) {

        ModelMap modelo = new ModelMap();

        Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
        if (jugador == null) {
            return new ModelAndView("redirect:/login");
        }
        modelo.put("jugador", jugador);
        modelo.put("idUsuario", jugador.getId());
        modelo.put("modoJuego", modoJuego);

        List<Partida> p = servicioPartida.obtenerPartidasAbiertasPorModo(modoJuego);

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

        if(modoJuego.equals(TIPO_PARTIDA.SUPERVIVENCIA)) {
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
        return new ModelAndView("ruletaCategoria", modelo);
    }

    @GetMapping("/pregunta")
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

        // Manejo especial para "CORONA"
        if ("CORONA".equalsIgnoreCase(categoria)) {
            return new ModelAndView("elegirCategoria", modelo);
        } else if ("GENERAL".equalsIgnoreCase(categoria)) {
            Pregunta pregunta = servicioPartida.buscarPartidaPorId(idPartida).getPreguntaActual();
            if(pregunta == null) {
                servicioPartida.obtenerPreguntaSupervivencia(idPartida);
                pregunta = servicioPartida.buscarPartidaPorId(idPartida).getPreguntaActual();
            }
            modelo.put("pregunta", pregunta);
            modelo.put("respondida", false);
            return new ModelAndView("preguntas", modelo);
        }

        // Validar que categoria sea válida para enum
        boolean valida = Arrays.stream(CATEGORIA_PREGUNTA.values())
                .anyMatch(c -> c.name().equalsIgnoreCase(categoria));

        if (!valida) {
            return new ModelAndView("redirect:/errorCategoria");
        }

        CATEGORIA_PREGUNTA catEnum = CATEGORIA_PREGUNTA.valueOf(categoria.toUpperCase());
        Pregunta p = servicioPartida.obtenerPregunta(catEnum, idUsuario);

        modelo.put("respondida", false);

        modelo.put("pregunta", p);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\nPregunta: " + p.getEnunciado() + "\n\n\n\n\n\n\n\n\n\n\n\n\nrespuestas:" + p.getRespuestas().size());
        return new ModelAndView("preguntas", modelo);
    }


    @PostMapping("/validar-respuesta")
    public ModelAndView validarRespuesta(
            @RequestParam("respuesta") Long idrespuestaSeleccionada,
            @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("idUsuario") Long idUsuario,
            HttpServletRequest request) {

        Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);
        Pregunta actual = servicioPartida.buscarPartidaPorId(idPartida).getPreguntaActual();

        System.out.println("Pregunta actual: " + (actual == null ? "null" : actual.getEnunciado()));

        // Guardar respuesta, obtener pregunta siguiente o null si termina
        Pregunta preguntaSiguiente = servicioPartida.validarRespuesta(idrespuestaSeleccionada, idPartida, modoJuego, usuario);

        boolean terminoPartida = (preguntaSiguiente == null);

        Respuesta respuestaSeleccionada = null;
        if (idrespuestaSeleccionada != -1) {
            respuestaSeleccionada = servicioPartida.buscarRespuestaPorId(idrespuestaSeleccionada);
        } else {
            // Creamos respuesta dummy para evitar null en la vista y que pinte el botón incorrecto
            respuestaSeleccionada = new Respuesta();
            respuestaSeleccionada.setId(-1L);
            respuestaSeleccionada.setDescripcion("Sin responder");
        }
        ModelMap modelo = new ModelMap();
        modelo.put("pregunta", actual);
        modelo.put("preguntaSiguiente", preguntaSiguiente);
        modelo.put("idRespuestaSeleccionada", respuestaSeleccionada);
        modelo.put("respuestaCorrecta", actual.getRespuestas().stream()
                .filter(r -> Boolean.TRUE.equals(r.getOpcionCorrecta()))
                .findFirst()
                .orElse(null));
        modelo.put("modoJuego", modoJuego);
        modelo.put("idUsuario", idUsuario);
        modelo.put("idPartida", idPartida);
        modelo.put("categoria", actual.getTipoPregunta().name());
        modelo.put("respondida", true);
        modelo.put("terminoPartida", terminoPartida);

        // Comprobar si ambos respondieron o no
        boolean ambosRespondieron = servicioPartida.chequearAmbosRespondieron(idPartida, usuario);
        modelo.put("avanzarAutomaticamente", ambosRespondieron && !terminoPartida);

        // Enviar mensaje WebSocket para actualizar al rival, o ambos
        servicioPartida.notificarEstadoPartida(idPartida, usuario, ambosRespondieron, terminoPartida);

        return new ModelAndView("preguntas", modelo);
    }

    @PostMapping("/siguientePregunta")
    public ModelAndView siguientePregunta(@RequestParam("idPreguntaSiguiente") Long idPreguntaSiguiente,
                                          @RequestParam("categoria") String categoria,
                                          @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
                                          @RequestParam("idPartida") Long idPartida,
                                          @RequestParam("idUsuario") Long idUsuario,
                                          HttpServletRequest request){
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

        // Siempre buscar la pregunta actual actualizada
        Pregunta p = servicioPartida.buscarPartidaPorId(idPartida).getPreguntaActual();
        modelo.put("pregunta", p);

        return new ModelAndView("preguntas", modelo);
    }


//    @Autowired
//    private SimpUserRegistry simpUserRegistry;
//
//    private void debugUsuariosConectados() {
//        System.out.println("🔍 Usuarios actualmente conectados por WebSocket:");
//        for (SimpUser user : simpUserRegistry.getUsers()) {
//            System.out.println(" - " + user.getName());
//        }
//    }
//    @MessageMapping("/crearOUnirsePartida")
//    public void crearOUnirsePartidaWS(@Payload PartidaRequest partidaRequest, Principal principal) {
//
//        System.out.println("🧩 [crearOUnirsePartida] Principal conectado: " + principal.getName());
//        System.out.println("🧩 [crearOUnirsePartida] Datos recibidos: " + partidaRequest);
//
//        Usuario jugador = servicioUsuario.buscarUsuarioPorId(partidaRequest.getUsuarioId());
//        if (jugador == null) return;
//
//        Partida partida = servicioPartida.crearOUnirsePartida(jugador, partidaRequest.getModoJuego());
//        List<Usuario> jugadores = servicioPartida.obtenerJugadoresEnPartida(partida.getId());
//
//        System.out.println("👤 Principal en WS: " + principal);
//        System.out.println("👤 Nombre Principal: " + principal.getName());
//        System.out.println("👤 Jugador esperado: " + jugador.getNombreUsuario());
//
//        // Notificar a todos los jugadores en la partida, excepto al que hizo la petición
//        for (Usuario u : jugadores) {
//            if (u.getNombreUsuario().equals(principal.getName())) {
//                // Saltar al propio jugador para no enviarse a sí mismo como rival
//                continue;
//            }
//
//            JugadorDTO dto = new JugadorDTO(u);
//            dto.setLinkAvatar(servicioUsuario.obtenerImagenAvatarSeleccionado(u.getId()));
//            dto.setIdPartida(partida.getId());
//            String destinatario = u.getNombreUsuario();
//
//            System.out.println("🧩 Emparejando con: " + u.getNombreUsuario());
//            System.out.println("📨 Enviando mensaje a: " + destinatario);
//
//            messagingTemplate.convertAndSendToUser(
//                    destinatario,
//                    "/queue/partida",
//                    dto
//            );
//        }
//
//        debugUsuariosConectados();
//    }



    /*
    @GetMapping("/preguntas")
    public ModelAndView preguntas() {
        return new ModelAndView("preguntas");
    }


    private Map<Integer, Pregunta> obtenerPreguntas() {
        Map<Integer, Pregunta> preguntas = new LinkedHashMap<>();

        preguntas.put(1, new Pregunta("¿Color favorito?", Arrays.asList("Rojo", "Verde", "Azul"), TIPO_PREGUNTA.ENTRETENIMIENTO));

        return preguntas;
    }
    */

}
