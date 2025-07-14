package com.tallerwebi.controller;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.impl.RepositorioAvatarImpl;
import com.tallerwebi.service.ServicioAvatar;
import com.tallerwebi.util.SessionUtil;
import org.apache.maven.lifecycle.internal.LifecycleStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/avatar")
public class AvatarController {

    private final SessionUtil sessionUtil;
    private final ServicioAvatar servicioAvatar;

    @Autowired
    public AvatarController(SessionUtil sessionUtil, ServicioAvatar servicioAvatar) {
        this.sessionUtil = sessionUtil;
        this.servicioAvatar = servicioAvatar;
    }

    @GetMapping
    public ModelAndView cargarAvatar(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Usuario usuarioEnSesion = sessionUtil.getUsuarioLogueado(request);
        List<Avatar> avataresDisponibles = servicioAvatar.obtenerAvataresDisponibles(usuarioEnSesion.getId());
        List<Avatar> avataresBloqueados = servicioAvatar.obtenerAvataresNoDisponibles(usuarioEnSesion.getId());

        if (usuarioEnSesion == null) {
            return new ModelAndView("redirect:/login");
        }

        Usuario logueado = servicioAvatar.buscarUsuarioPorId(usuarioEnSesion.getId());

        model.addAttribute("usuario", logueado);
        model.addAttribute("avataresDisponibles", avataresDisponibles);
        model.addAttribute("avataresBloqueados", avataresBloqueados);

        System.out.println("\n\n\n-------------------------------------------------------DESBLOQUEADOS\n\n" + avataresBloqueados);

        System.out.println("\n\n\n-------------------------------------------------------BLOQUEADOS\n\n" + avataresBloqueados);

        return new ModelAndView("avatar", model);
    }

    @PostMapping("/actualizarAvatar")
    public ModelAndView actualizarAvatar(HttpServletRequest request, @RequestParam("avatar") Long idAvatar) {
        ModelMap model = new ModelMap();

        Usuario usuarioEnSesion = sessionUtil.getUsuarioLogueado(request);

        servicioAvatar.cambiarAvatar(idAvatar, usuarioEnSesion.getId());

        // 👇 Refrescamos el usuario desde la BD
        Usuario actualizado = servicioAvatar.buscarUsuarioPorId(usuarioEnSesion.getId());

        // 👇 Actualizamos también en la sesión
        request.getSession().setAttribute("usuario", actualizado);

        List<Avatar> avataresDisponibles = servicioAvatar.obtenerAvataresDisponibles(actualizado.getId());
        List<Avatar> avataresBloqueados = servicioAvatar.obtenerAvataresNoDisponibles(actualizado.getId());

        model.addAttribute("usuario", actualizado);
        model.addAttribute("avataresDisponibles", avataresDisponibles);
        model.addAttribute("avataresBloqueados", avataresBloqueados);

        return new ModelAndView("avatar", model);
    }



    @PostMapping("/desbloquear")
    public String desbloquearAvatar(@RequestParam Long idAvatar, HttpServletRequest request) {
        Usuario usuario = sessionUtil.getUsuarioLogueado(request);

        servicioAvatar.comprarAvatar(idAvatar, usuario.getId());

        // 🔄 refrescar el usuario actualizado
        Usuario actualizado = servicioAvatar.buscarUsuarioPorId(usuario.getId());
        request.getSession().setAttribute("usuario", actualizado);

        return "redirect:/avatar";
    }

    /*@PostMapping("/desbloquear")
    public String desbloquearAvatar(@RequestParam Long idAvatar, HttpServletRequest request) {
        Usuario usuario = sessionUtil.getUsuarioLogueado(request);

        servicioAvatar.comprarAvatar(idAvatar, usuario.getId());

        return "redirect:/avatar";
    }*/
}
