package com.tallerwebi.controller;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioTrampaUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MisTrampasController {

    private final ServicioTrampaUsuario servicioTrampaUsuario;

    @Autowired
    public MisTrampasController(ServicioTrampaUsuario servicioTrampaUsuario) {
        this.servicioTrampaUsuario = servicioTrampaUsuario;
    }

    @GetMapping("/misTrampas")
    public ModelAndView mostrarMisTrampas(HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("USUARIO");

        List<Trampa> trampas = servicioTrampaUsuario.obtenerTrampasDelUsuario(usuarioLogueado.getId());

        ModelMap model = new ModelMap();
        model.addAttribute("misTrampas", trampas);

        return new ModelAndView("misTrampas", model);
    }
}

