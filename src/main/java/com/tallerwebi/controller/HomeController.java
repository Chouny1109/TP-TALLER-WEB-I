package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioRecovery;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final IServicioUsuario servicioUsuario;

    @Autowired
    public HomeController(IServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping
    public ModelAndView irAHome(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("home");
        Usuario usuario = (Usuario) request.getSession().getAttribute("USUARIO");
        if (usuario != null) {
            mav.addObject("nombreUsuario", usuario.getNombreUsuario());
            mav.addObject("nivel", usuario.getNivel());
            mav.addObject("monedas", usuario.getMonedas());
            mav.addObject("vidas", usuario.getVidas());
        } else {
            return new ModelAndView("redirect:/login");
        }

        String avatarImg = this.servicioUsuario.obtenerImagenAvatarSeleccionado(usuario.getId());
        mav.addObject("avatarImg", avatarImg);
        mav.addObject("modos", TIPO_PARTIDA.values());
        return mav;
    }

}
