package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioPartida;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PartidaController(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }

    @GetMapping("/cargar")
    public ModelAndView cargarPartida(HttpServletRequest request, @RequestParam("modoJuego") TIPO_PARTIDA modoJuego) {

        ModelMap modelo = new ModelMap();

        Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
        modelo.put("jugador", jugador);

        Partida partida = servicioPartida.crearOUnirsePartida(jugador, modoJuego);
        modelo.put("partida", partida);

        return new ModelAndView("cargarPartida", modelo);
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
