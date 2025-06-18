package com.tallerwebi.controller;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/misiones")
public class MisionesController {

    private final ServicioMisionesUsuario servicioMisionesUsuario;
    private final SessionUtil sessionUtil;

    @Autowired
    public MisionesController(ServicioMisionesUsuario servicioMisionesUsuario, SessionUtil sessionUtil) {
        this.servicioMisionesUsuario = servicioMisionesUsuario;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping
    public ModelAndView misiones(HttpServletRequest request) throws UsuarioNoExistente {
        ModelMap modelMap = new ModelMap();

        Usuario logueado = this.sessionUtil.getUsuarioLogueado(request);

        List<Mision> misionesDelUsuario = this.servicioMisionesUsuario.obtenerLasMisionesDelUsuarioPorId(logueado.getId());

        modelMap.addAttribute("misiones", misionesDelUsuario);

        return new ModelAndView("misiones", modelMap);
    }
}
