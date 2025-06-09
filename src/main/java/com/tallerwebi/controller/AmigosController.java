package com.tallerwebi.controller;

import com.tallerwebi.service.ServicioAmigos;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/amigos")
public class AmigosController {

    private final ServicioAmigos servicioAmigos;

    public AmigosController(ServicioAmigos servicioAmigos) {
        this.servicioAmigos = servicioAmigos;
    }

    @GetMapping
    public ModelAndView verAmigos() {
        ModelAndView mav = new ModelAndView("amigos");
        mav.addObject("amigos", servicioAmigos.obtenerAmigos());
        return mav;
    }
}
