package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.ROL_USUARIO;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioDTO;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioEditor;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {


    private IServicioUsuario servicioUsuario;

    @Autowired
    public AdminController(IServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }


    @GetMapping
    public String vistaAdmin(Model model) {
        long totalUsuarios = servicioUsuario.obtenerCantidadDeUsuarios();
        List<Usuario> listaUsuarios = servicioUsuario.obtenerTodosLosUsuarios(); // agregar esta línea

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuarios", listaUsuarios); // y esta línea

        return "admin";
    }


    @PostMapping("/banear/{id}")
    public String banearUsuario(@PathVariable Long id) {
        servicioUsuario.banearUsuario(id);
        return "redirect:/admin";
    }

    @PostMapping("/desbanear/{id}")
    public String desbanearUsuario(@PathVariable Long id) {
        servicioUsuario.desbanearUsuario(id);
        return "redirect:/admin";
    }

    @PostMapping("/rol/{id}")
    public String cambiarRol(@PathVariable Long id, @RequestParam("nuevoRol") String nuevoRol) {
        servicioUsuario.asignarRol(id, ROL_USUARIO.valueOf(nuevoRol.toUpperCase()));
        return "redirect:/admin";
    }
}
