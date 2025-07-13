package com.tallerwebi.controller;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.Vida;
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
import java.util.List;

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

        List<Long> avatarIdsDelUsuario = servicioUsuario.obtenerIdsAvataresDelUsuario(usuarioActualizado.getId());
        modelo.put("avatarIdsDelUsuario", avatarIdsDelUsuario);

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

        setearUsuarioEnSession(session, usuarioActualizado);

        redirectAttrs.addFlashAttribute("mensaje", "¡Has comprado la trampa exitosamente!");
        return new ModelAndView("redirect:/tienda");
    }

    @GetMapping("/comprar-avatar/{idAvatar}")
    public ModelAndView comprarAvatar(@PathVariable Long idAvatar, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para comprar avatares.");
            return new ModelAndView("redirect:/login");
        }

        Usuario usuarioActualizado = servicioUsuario.buscarUsuarioPorId(usuario.getId());
        Avatar avatar = servicioTienda.obtenerAvatar(idAvatar);

        if (servicioUsuario.usuarioTieneAvatar(usuarioActualizado.getId(), idAvatar)) {
            redirectAttributes.addFlashAttribute("error", "Ya tienes este avatar.");
            return new ModelAndView("redirect:/tienda");
        }

        if (!servicioUsuario.tieneMonedasSuficientes(usuarioActualizado.getId(), avatar.getValor())) {
            redirectAttributes.addFlashAttribute("error", "No tienes suficientes monedas para comprar este avatar.");
            return new ModelAndView("redirect:/tienda");
        }
        servicioTienda.asignarAvatarAUsuario(usuarioActualizado, avatar);
        servicioUsuario.descontarMonedas(usuarioActualizado.getId(), avatar.getValor());

        setearUsuarioEnSession(session, usuarioActualizado);

        redirectAttributes.addFlashAttribute("mensaje", "¡Has comprado el avatar exitosamente!");
        return new ModelAndView("redirect:/tienda");
    }

    @GetMapping("/comprar-vida/{idVida}")
    public ModelAndView comprarVida(@PathVariable Long idVida, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para comprar vidas.");
            return new ModelAndView("redirect:/login");
        }

        Usuario usuarioActualizado = servicioUsuario.buscarUsuarioPorId(usuario.getId());
        Vida vida = servicioTienda.obtenerVidaPorId(idVida);

        if (!servicioUsuario.tieneMonedasSuficientes(usuarioActualizado.getId(), vida.getValor())) {
            redirectAttributes.addFlashAttribute("error", "No tienes suficientes monedas para comprar este pack de vidas.");
            return new ModelAndView("redirect:/tienda");
        }

        servicioUsuario.descontarMonedas(usuarioActualizado.getId(), vida.getValor());
        servicioUsuario.agregarVidas(usuarioActualizado.getId(), vida.getCantidad());

        setearUsuarioEnSession(session, usuarioActualizado);

        redirectAttributes.addFlashAttribute("mensaje", "¡Has comprado " + vida.getCantidad() + " vidas exitosamente!");
        return new ModelAndView("redirect:/tienda");
    }

    private void setearUsuarioEnSession(HttpSession session, Usuario usuario) {
        session.setAttribute("USUARIO", servicioUsuario.buscarUsuarioPorId(usuario.getId()));
    }

}
