package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.PartidaRequest;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioPartida;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/partida")
public class PartidaController {

    private ServicioPartida servicioPartida;
    private ServicioUsuario servicioUsuario;
    @Autowired
    public PartidaController(ServicioPartida servicioPartida, ServicioUsuario servicioUsuario) {
        this.servicioPartida = servicioPartida;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/cargar")
    public ModelAndView cargarPartida(HttpServletRequest request, @RequestParam("modoJuego") TIPO_PARTIDA modoJuego) {

        ModelMap modelo = new ModelMap();

        Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
        if (jugador == null) {
            return new ModelAndView("redirect:/login");
        }
        modelo.put("jugador", jugador);

        Partida partida = servicioPartida.crearOUnirsePartida(jugador, modoJuego);
        modelo.put("partida", partida);
        String avatarImg = this.servicioUsuario.obtenerImagenAvatarSeleccionado(jugador.getId());
        modelo.put("avatarImg", avatarImg);

        return new ModelAndView("cargarPartida", modelo);
    }

    @MessageMapping("/crearOUnirsePartida")
    public void crearOUnirsePartidaWS(@Payload PartidaRequest partidaRequest) {
        // Obtener el usuario real desde el repositorio usando el id recibido
        Usuario jugador = servicioUsuario.buscarUsuarioPorId(partidaRequest.getUsuarioId());
        if (jugador != null) {
            servicioPartida.crearOUnirsePartida(jugador, partidaRequest.getModoJuego());
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
