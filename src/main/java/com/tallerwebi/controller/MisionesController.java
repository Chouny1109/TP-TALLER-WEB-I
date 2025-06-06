package com.tallerwebi.controller;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioMisiones;
import com.tallerwebi.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/misiones")
public class MisionesController {

    private final ServicioMisiones servicioMisiones;
    private final SessionUtil sessionUtil;

    public MisionesController(ServicioMisiones servicioMisiones, SessionUtil sessionUtil) {
        this.servicioMisiones = servicioMisiones;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping
    public ModelAndView misiones(HttpServletRequest request) throws UsuarioNoExistente {

        ModelMap modelMap = new ModelMap();

        Usuario logueado = this.sessionUtil.getUsuarioLogueado(request);

        List<Mision> misionesDelUsuario = this.servicioMisiones.obtenerLasMisionesDelUsuario(logueado.getId());

        modelMap.addAttribute("misiones", misionesDelUsuario);

        return new ModelAndView("misiones", modelMap);
    }
}
