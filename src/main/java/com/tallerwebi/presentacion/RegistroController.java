package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistroController {

    private ServicioLogin servicioLogin;

    @Autowired
    public RegistroController(ServicioLogin servicioLogin){
        this.servicioLogin = servicioLogin;
    }




    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioRegistro() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("nuevo-usuario", model);
    }


    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario,
                                    @RequestParam("passConfirm") String passConfirm) {
        ModelMap model = new ModelMap();

        if (!usuario.getPassword().equals(passConfirm)) {
            model.put("error", "Las contraseñas no coinciden");
            return new ModelAndView("nuevo-usuario", model);  // Regresa al formulario con el mensaje de error
        }
        String email = usuario.getEmail();
        if (!email.contains("@") || !email.contains(".com")) {
            model.put("error", "El email debe contener '@' y '.com'");
            return new ModelAndView("nuevo-usuario", model);  // Regresa al formulario con el mensaje de error
        }
        try {
            servicioLogin.registrar(usuario);  // Registra el usuario
        } catch (UsuarioExistente e) {
            model.put("error", "El usuario ya existe");
            return new ModelAndView("nuevo-usuario", model);  // Regresa al formulario con mensaje de error
        } catch (Exception e) {
            model.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("nuevo-usuario", model);  // Regresa al formulario con mensaje de error
        }
        return new ModelAndView("redirect:/login");  // Redirige al login después de registrar
    }

}

