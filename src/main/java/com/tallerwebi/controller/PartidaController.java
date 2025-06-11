package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;
import com.tallerwebi.service.ServicioPartida;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @MessageMapping("/crearOUnirsePartida")
    public void crearOUnirsePartidaWS(@Payload PartidaRequest partidaRequest) {
        Usuario jugador = servicioUsuario.buscarUsuarioPorId(partidaRequest.getUsuarioId());
        if (jugador == null) return;

        Partida partida = servicioPartida.crearOUnirsePartida(jugador, partidaRequest.getModoJuego());
        List<Usuario> jugadores = servicioPartida.obtenerJugadoresEnPartida(partida.getId());

        // Notificar a todos los jugadores en la partida
        for (Usuario u : jugadores) {
            Usuario usuarioEnviar = u.equals(jugador) ? jugador : u;
            JugadorDTO dto = new JugadorDTO(usuarioEnviar);
            System.out.println("Enviando mensaje a usuario: " + usuarioEnviar.getNombreUsuario() + " con datos: " + dto);
            dto.setLinkAvatar(this.servicioUsuario.obtenerImagenAvatarSeleccionado(usuarioEnviar.getId()));
            messagingTemplate.convertAndSendToUser(
                    usuarioEnviar.getNombreUsuario(),
                    "/queue/partida",
                    dto
            );
        }}


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
