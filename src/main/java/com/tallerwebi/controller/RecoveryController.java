package com.tallerwebi.controller;

import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.TokenInvalido;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.DatosRecovery;
import com.tallerwebi.model.RecoveryToken;
import com.tallerwebi.model.Usuario;

import com.tallerwebi.service.ServicioRecovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/recovery")
public class RecoveryController {

    private ServicioRecovery servicioRecovery;

    @Autowired
    public RecoveryController(ServicioRecovery servicioRecovery) {
        this.servicioRecovery = servicioRecovery;
    }

    @GetMapping
    public ModelAndView cargarRecovery() {
        ModelAndView mv = new ModelAndView("recovery");
        mv.addObject("datosRecovery", new DatosRecovery());  // Le pasás el DTO vacío para el binding
        return mv;
    }


    @PostMapping("/send-email")
    public ModelAndView enviarEmailDeRecuperacion(@ModelAttribute DatosRecovery datosRecovery) throws UsuarioNoExistente {
        ModelAndView mv = new ModelAndView();

        String email = datosRecovery.getEmail();
        try {
            boolean enviado = servicioRecovery.enviarEmailDeRecuperacion(email);
            mv.setViewName("sendEmail");

            mv.addObject("datosRecovery", datosRecovery);
            mv.addObject("mensaje", "Revisá tu casilla de correo para continuar.");
            return mv;
        }catch (UsuarioNoExistente e) {
            mv.setViewName("recovery");
            mv.addObject("datosRecovery", new DatosRecovery());

            mv.addObject("error", "El email no existe en nuestra base.");
            return mv;
        }


    }

    @GetMapping("/cambio-de-contrasena")
    public ModelAndView mostrarFormularioCambioPassword(@RequestParam("token") String token) {
        ModelAndView mv = new ModelAndView();

        RecoveryToken recoveryToken = servicioRecovery.obtenerToken(token);

        if (recoveryToken == null) {
            mv.setViewName("error");
            mv.addObject("mensaje", "Token inválido o expirado.");
            return mv;
        }

        DatosRecovery datosRecovery = new DatosRecovery();
        datosRecovery.setEmail(recoveryToken.getEmail()); // opcional, para prellenar el email

        mv.setViewName("recovery2"); // nombre de la vista que tiene el formulario de nueva contraseña
        mv.addObject("datosRecovery", datosRecovery);
        mv.addObject("token", token);

        return mv;
    }

    @PostMapping("/cambio-de-contrasena")
    public ModelAndView cambiarPassword(@ModelAttribute DatosRecovery datosRecovery,
                                        @RequestParam("token") String token) throws PasswordsNotEquals, UsuarioNoExistente {

        System.out.println("Email recibido: " + datosRecovery.getEmail());
        ModelAndView mv = new ModelAndView();
        RecoveryToken recoveryToken = servicioRecovery.obtenerToken(token);


        try {
            Usuario usuario = servicioRecovery.cambiarPassword(datosRecovery, token);
            mv.setViewName("login");
            mv.addObject("mensaje", "Contraseña cambiada con éxito. Por favor, inicia sesión.");
            return mv;
        } catch (PasswordsNotEquals e) {
            mv.setViewName("recovery2");
            mv.addObject("datosRecovery", datosRecovery);
            mv.addObject("token", token);
            mv.addObject("error", "Las contraseñas no coinciden");
            return mv;
        } catch (UsuarioNoExistente e) {
            mv.setViewName("recovery2");
            mv.addObject("datosRecovery", datosRecovery);
            mv.addObject("token", token);
            mv.addObject("error", "Usuario no encontrado.");
            return mv;
        } catch (EmailInvalido e) {
            mv.setViewName("recovery");
            mv.addObject("datosRecovery", datosRecovery);
            mv.addObject("token", token);
            mv.addObject("error", "Email inválido.");
            return mv;

        } catch (TokenInvalido e) {
            mv.setViewName("recovery");
            mv.addObject("datosRecovery", datosRecovery);
            mv.addObject("token", token);
            mv.addObject("error", "Token nulo.");
            return mv;
        }
    }
}
