package com.tallerwebi.controller;

import org.springframework.ui.Model;
import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioMisTrampas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class MisTrampasController {

    private final ServicioMisTrampas servicioMisTrampas;

    @Autowired
    public MisTrampasController(ServicioMisTrampas servicioMisTrampas) {
        this.servicioMisTrampas = servicioMisTrampas;
    }

    @GetMapping("/mis-trampas")
    public String mostrarMisTrampas(Model modelo, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

        List<Trampa> trampas = servicioMisTrampas.obtenerTrampasPorUsuario(usuarioLogueado);
        modelo.addAttribute("misTrampas", trampas);

        return "mis-trampas"; // nombre del archivo HTML sin extensión
    }
}

