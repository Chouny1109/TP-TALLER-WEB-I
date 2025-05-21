package com.tallerwebi.controller;

import com.tallerwebi.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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

}
