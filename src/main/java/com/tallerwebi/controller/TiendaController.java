package com.tallerwebi.controller;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioTienda;
import com.tallerwebi.service.ServicioTrampaUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    private final ServicioTienda servicioTienda;
    private final IServicioUsuario servicioUsuario;
    private final ServicioTrampaUsuario servicioTrampaUsuario;

    @Autowired
    public TiendaController(ServicioTienda servicioTienda, IServicioUsuario servicioUsuario, ServicioTrampaUsuario servicioTrampaUsuario) {
        this.servicioTienda = servicioTienda;
        this.servicioUsuario = servicioUsuario;
        this.servicioTrampaUsuario = servicioTrampaUsuario;
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

    @GetMapping("/comprar-trampa/{idTrampa}")
    public ModelAndView comprarTrampa(@PathVariable Long idTrampa, HttpSession session, RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) session.getAttribute("USUARIO");
        if (usuario == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para comprar trampas.");
            return new ModelAndView("redirect:/login");
        }

        Usuario usuarioActualizado = servicioUsuario.buscarUsuarioPorId(usuario.getId());
        Trampa trampa = servicioTienda.obtenerTrampaPorId(idTrampa);

        if (!servicioUsuario.tieneMonedasSuficientes(usuarioActualizado.getId(), trampa.getValor())) {
            redirectAttrs.addFlashAttribute("error", "No tienes suficientes monedas para comprar esta trampa.");
            return new ModelAndView("redirect:/tienda");
        }

        servicioUsuario.descontarMonedas(usuarioActualizado.getId(), trampa.getValor());
        servicioTrampaUsuario.asignarTrampaAUsuario(usuarioActualizado, trampa);

        redirectAttrs.addFlashAttribute("mensaje", "¡Has comprado la trampa exitosamente!");
        return new ModelAndView("redirect:/tienda");
    }


}
