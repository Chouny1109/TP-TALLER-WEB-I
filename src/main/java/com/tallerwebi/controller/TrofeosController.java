package com.tallerwebi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trofeos")
public class TrofeosController {

    @GetMapping
    public String cargarTienda() {
        return "trofeos";
    }

}
