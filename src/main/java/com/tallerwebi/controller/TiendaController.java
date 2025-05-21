package com.tallerwebi.controller;

import com.tallerwebi.service.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    private final ServicioTienda servicioTienda;

    @Autowired
    public TiendaController(ServicioTienda servicioTienda) {
        this.servicioTienda = servicioTienda;
    }

    @GetMapping
    public ModelAndView cargarTienda() {
        ModelMap modelo = new ModelMap();

        modelo.put("trampas", servicioTienda.obtenerTrampas());
        modelo.put("vidas", servicioTienda.obtenerVidas());
        modelo.put("monedas", servicioTienda.obtenerMonedas());
        modelo.put("avatares", servicioTienda.obtenerAvatares());

        return new ModelAndView("tienda", modelo);
    }

}
