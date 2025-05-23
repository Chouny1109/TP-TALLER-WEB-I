package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PREGUNTA;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/partida")
public class PartidaController {

    @GetMapping("/cargar")
    public ModelAndView cargarPartida(HttpServletRequest request) {

        ModelMap modelo = new ModelMap();

        Usuario jugador = (Usuario) request.getSession().getAttribute("USUARIO");
        modelo.put("jugador", jugador);

        return new ModelAndView("cargarPartida", modelo);
    }

    @GetMapping("/preguntas")
    public ModelAndView preguntas() {
        ModelMap modelo = new ModelMap();
        Map<Integer, Pregunta> preguntas = obtenerPreguntas();
        modelo.put("preguntas", preguntas);

        return new ModelAndView("preguntas", modelo);
    }

    private Map<Integer, Pregunta> obtenerPreguntas() {
        Map<Integer, Pregunta> preguntas = new LinkedHashMap<>();

        preguntas.put(1, new Pregunta("¿Color favorito?", Arrays.asList("Rojo", "Verde", "Azul"), TIPO_PREGUNTA.ENTRETENIMIENTO));

        return preguntas;
    }

}
