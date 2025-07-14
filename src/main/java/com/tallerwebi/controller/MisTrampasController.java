package com.tallerwebi.controller;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.TrampaUsuario;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioTrampaUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class MisTrampasController {

    private final ServicioTrampaUsuario servicioTrampaUsuario;

    @Autowired
    public MisTrampasController(ServicioTrampaUsuario servicioTrampaUsuario) {
        this.servicioTrampaUsuario = servicioTrampaUsuario;
    }

    @GetMapping("/misTrampas")
    public ModelAndView mostrarMisTrampas(HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("USUARIO");
        List<TrampaUsuario> trampasUsuario = servicioTrampaUsuario.obtenerTrampasDelUsuario(usuarioLogueado.getId());
        Map<String, Trampa> trampasPorDia = servicioTrampaUsuario.obtenerTrampasPorDia();

        List<String> diasSemana = List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");

        ModelMap model = new ModelMap();
        model.addAttribute("misTrampas", trampasUsuario);
        model.addAttribute("trampasPorDia", trampasPorDia);
        model.addAttribute("diasSemana", diasSemana);

        return new ModelAndView("misTrampas", model);
    }

    @GetMapping("/reclamar-trampa")
    public String reclamarTrampa(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO");
        if (usuario == null) {
            return "redirect:/login";
        }

        String diaActual = LocalDate.now()
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

        diaActual = diaActual.substring(0, 1).toUpperCase() + diaActual.substring(1);
        Trampa trampaDelDia = servicioTrampaUsuario.obtenerTrampasPorDia().get(diaActual);
        if (trampaDelDia == null) {
            return "redirect:/misTrampas";
        }

        boolean reclamo = servicioTrampaUsuario.reclamarTrampaDelDia(usuario, trampaDelDia);

        if (reclamo) {
            redirectAttributes.addFlashAttribute("mensaje", "🎉 ¡Reclamaste la trampa del día!");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "⚠️ Ya reclamaste la trampa de hoy.");
        }

        return "redirect:/misTrampas";
    }


}

