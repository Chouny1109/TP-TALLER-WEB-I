package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public ModelAndView irAHome(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("home");
        Usuario usuario = (Usuario) request.getSession().getAttribute("USUARIO");
        if (usuario != null) {
            mav.addObject("nombreUsuario", usuario.getNombreUsuario());
        }

        mav.addObject("modos", TIPO_PARTIDA.values());
        return mav;
    }

}
