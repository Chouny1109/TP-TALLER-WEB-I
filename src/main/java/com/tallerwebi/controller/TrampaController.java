package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.service.ServicioTrampaUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/trampa")
public class TrampaController {

    @Autowired
    private ServicioTrampaUsuario servicioTrampaUsuario;

    @PostMapping("/usar")
    public String usarTrampa(
            @RequestParam("idTrampa") Long idTrampa,
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("categoria") String categoria,
            @RequestParam("modoJuego") TIPO_PARTIDA modoJuego,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        servicioTrampaUsuario.consumirTrampa(idUsuario, idTrampa);
        session.setAttribute("trampaActiva", idTrampa);

        redirectAttributes.addAttribute("id", idPartida);
        redirectAttributes.addAttribute("idUsuario", idUsuario);
        redirectAttributes.addAttribute("categoria", categoria);
        redirectAttributes.addAttribute("modoJuego", modoJuego);

        return "redirect:/partida/pregunta";
    }

}
