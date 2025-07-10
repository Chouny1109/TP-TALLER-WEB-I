package com.tallerwebi.controller;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/avatar")
public class AvatarController {

    private final SessionUtil sessionUtil;

    @Autowired
    public AvatarController(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @GetMapping
    public ModelAndView cargarAvatar(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Usuario logueado = sessionUtil.getUsuarioLogueado(request);

        if (logueado == null) {
            return new ModelAndView("redirect:/login");
        }

        model.addAttribute("usuario", logueado);
        return new ModelAndView("avatar", model);
    }

    @PostMapping
    public String actualizarAvatar(Model model) {
        return "redirect:/tienda";
    }

}
