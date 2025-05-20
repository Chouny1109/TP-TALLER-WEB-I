package com.tallerwebi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/amigos")
public class AmigosController {

    @GetMapping
    public String verAmigos(Model model) {

        return "amigos";
    }
}


