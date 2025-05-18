package com.tallerwebi.controller;

import com.tallerwebi.components.InputField;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioSetting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final ServicioSetting servicioSetting;

    public SettingsController(ServicioSetting servicioSetting) {
        this.servicioSetting = servicioSetting;
    }

    @GetMapping()
    public ModelAndView cargarSettings(HttpServletRequest request) {

        ModelMap modelMap = new ModelMap();

        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("USUARIO");
        List<InputField> inputFields = this.servicioSetting.obtenerInputsForm(usuarioLogueado);

        modelMap.addAttribute("inputFields", inputFields);
        modelMap.addAttribute("usuarioLogueado", usuarioLogueado);

        return new ModelAndView("settings", modelMap);
    }

//    @PostMapping("/update")
//    public ModelAndView actualizarUsuario(@ModelAttribute("datosUpdate") Usuario usuario){
//
//    }
}

