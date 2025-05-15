package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Usuario;
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
        return mav;
    }

}
