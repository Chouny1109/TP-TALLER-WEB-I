package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.ROL_USUARIO;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioLogin;

import com.tallerwebi.model.DatosLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {
        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());

        if (usuarioBuscado != null) {
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            request.getSession().setAttribute("USUARIO", usuarioBuscado);
            model.put("nombreUsuario", usuarioBuscado.getNombreUsuario());

            if (usuarioBuscado.getRol() == ROL_USUARIO.ADMIN) {
                return new ModelAndView("redirect:/admin");
            } else if (usuarioBuscado.getRol() == ROL_USUARIO.EDITOR) {
                return new ModelAndView("redirect:/editor");
            }
            return new ModelAndView("redirect:/home");

        }
            model.put("error", "Usuario o clave incorrecta");

        return new ModelAndView("login", model);
    }


    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpServletRequest request) {
        servicioLogin.cerrarSesion(request);
        return "redirect:/login"; // Redirige al login u otra página
    }

}

