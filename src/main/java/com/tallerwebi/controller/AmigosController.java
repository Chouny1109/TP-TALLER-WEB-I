package com.tallerwebi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/amigos")
public class AmigosController {

    @GetMapping
    public ModelAndView verAmigos(Model model) {

        return new ModelAndView ("amigos");
    }
}


