package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/partida")
public class PartidaController {

    @GetMapping("/cargar")
    public String cargarPartida() {

        return "cargarPartida";
    }

}
