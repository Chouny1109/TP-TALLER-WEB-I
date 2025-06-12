package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;
import com.tallerwebi.service.ServicioPartida;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/partida")
public class PartidaController {

    private ServicioPartida servicioPartida;
    private ServicioUsuario servicioUsuario;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PartidaController(ServicioPartida servicioPartida, ServicioUsuario servicioUsuario, SimpMessagingTemplate messagingTemplate) {
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
        modelo.put("modoJuego", modoJuego);
        Partida partida = servicioPartida.crearOUnirsePartida(jugador, modoJuego);
        modelo.put("partida", partida);
        String avatarImg = this.servicioUsuario.obtenerImagenAvatarSeleccionado(jugador.getId());
        modelo.put("avatarImg", avatarImg);

        return new ModelAndView("cargarPartida", modelo);
    }

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    private void debugUsuariosConectados() {
        System.out.println("🔍 Usuarios actualmente conectados por WebSocket:");
        for (SimpUser user : simpUserRegistry.getUsers()) {
            System.out.println(" - " + user.getName());
        }
    }
    @MessageMapping("/crearOUnirsePartida")
    public void crearOUnirsePartidaWS(@Payload PartidaRequest partidaRequest, Principal principal) {

        System.out.println("🧩 [crearOUnirsePartida] Principal conectado: " + principal.getName());
        System.out.println("🧩 [crearOUnirsePartida] Datos recibidos: " + partidaRequest);

        Usuario jugador = servicioUsuario.buscarUsuarioPorId(partidaRequest.getUsuarioId());
        if (jugador == null) return;

        Partida partida = servicioPartida.crearOUnirsePartida(jugador, partidaRequest.getModoJuego());
        List<Usuario> jugadores = servicioPartida.obtenerJugadoresEnPartida(partida.getId());

        System.out.println("👤 Principal en WS: " + principal);
        System.out.println("👤 Nombre Principal: " + principal.getName());
        System.out.println("👤 Jugador esperado: " + jugador.getNombreUsuario());

        // Notificar a todos los jugadores en la partida, excepto al que hizo la petición
        for (Usuario u : jugadores) {
            if (u.getNombreUsuario().equals(principal.getName())) {
                // Saltar al propio jugador para no enviarse a sí mismo como rival
                continue;
            }

            JugadorDTO dto = new JugadorDTO(u);
            dto.setLinkAvatar(servicioUsuario.obtenerImagenAvatarSeleccionado(u.getId()));
            dto.setIdPartida(partida.getId());
            String destinatario = u.getNombreUsuario();

            System.out.println("🧩 Emparejando con: " + u.getNombreUsuario());
            System.out.println("📨 Enviando mensaje a: " + destinatario);

            messagingTemplate.convertAndSendToUser(
                    destinatario,
                    "/queue/partida",
                    dto
            );
        }

        debugUsuariosConectados();
    }


    @PostMapping("/finalizarPartida")
    public ResponseEntity<?> finalizarPartida(@RequestParam("idPartida") Long idPartida) {
        try {
            servicioPartida.finalizarPartida(idPartida);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al finalizar partida");
        }
    }



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
