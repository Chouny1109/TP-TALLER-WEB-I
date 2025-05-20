package com.tallerwebi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/recovery")
public class RecoveryController {

    @GetMapping
    public String cargarRecovery() {
        return "recovery";
    }

    @RequestMapping(path = "/send-email", method = RequestMethod.POST)
    public ModelAndView enviarEmailDeRecuperacion(@RequestParam("email") String email) {
        ModelAndView mv = new ModelAndView("sendEmail");
        mv.addObject("mensaje", "¡Éxito! Revisa tu casilla de correo para continuar con la recuperación de tu contraseña.");
        return mv;
    }
}
