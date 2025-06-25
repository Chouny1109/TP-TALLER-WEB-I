package com.tallerwebi.controller;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    private final ServicioTienda servicioTienda;
    private final IServicioUsuario servicioUsuario;

    @Autowired
    public TiendaController(ServicioTienda servicioTienda, IServicioUsuario servicioUsuario) {
        this.servicioTienda = servicioTienda;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping
    public ModelAndView cargarTienda(HttpSession session) {
        ModelMap modelo = new ModelMap();

        modelo.put("trampas", servicioTienda.obtenerTrampas());
        modelo.put("vidas", servicioTienda.obtenerVidas());
        modelo.put("monedas", servicioTienda.obtenerMonedas());
        modelo.put("avatares", servicioTienda.obtenerAvatares());

        Usuario usuario = (Usuario) session.getAttribute("USUARIO");

        Usuario usuarioActualizado = servicioUsuario.buscarUsuarioPorId(usuario.getId());

        modelo.put("misMonedas", usuarioActualizado.getMonedas());

        return new ModelAndView("tienda", modelo);
    }

}
