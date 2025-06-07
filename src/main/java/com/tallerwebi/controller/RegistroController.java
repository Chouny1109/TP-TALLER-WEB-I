package com.tallerwebi.controller;




import com.tallerwebi.dominio.enums.ROL_USUARIO;
import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.model.Usuario;

import com.tallerwebi.model.DatosRegistro;
import com.tallerwebi.service.ServicioRegistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistroController {

    private ServicioRegistro servicioRegistro;

    @Autowired
    public RegistroController(ServicioRegistro servicioRegistro) {
        this.servicioRegistro = servicioRegistro;
    }




    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioRegistro() {
        ModelMap model = new ModelMap();
        model.put("datosRegistro", new DatosRegistro());
        return new ModelAndView("nuevo-usuario", model);
    }


    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("datosRegistro") DatosRegistro datosRegistro,
                                    RedirectAttributes redirectAttributes) {
        ModelMap model = new ModelMap();

        Usuario usuario = new Usuario();
        usuario.setEmail(datosRegistro.getEmail());
        usuario.setPassword(datosRegistro.getPassword());
        usuario.setNombreUsuario(datosRegistro.getNombreUsuario());
        usuario.setRol(ROL_USUARIO.JUGADOR);

        try {
            servicioRegistro.registrar(usuario, datosRegistro.getConfirmarPassword());  // Registra el usuario
        } catch (UsuarioExistente e) {
            model.put("error", "El usuario ya existe");
            return new ModelAndView("nuevo-usuario", model);  // Regresa al formulario con mensaje de error
        } catch (EmailInvalido e) {
            model.put("error", "El email debe contener '@' y '.com'");
            return new ModelAndView("nuevo-usuario", model);
        } catch (PasswordsNotEquals e){
            model.put("error", "Las contraseñas no coinciden");
            return new ModelAndView("nuevo-usuario", model);
        }
        catch (Exception e) {
            model.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("nuevo-usuario", model);  // Regresa al formulario con mensaje de error
        }
        redirectAttributes.addFlashAttribute("exito", "¡Registrado correctamente!");
        return new ModelAndView("redirect:/login");  // Redirige al login después de registrar
    }

}

