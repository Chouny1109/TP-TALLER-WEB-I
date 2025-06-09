package com.tallerwebi.controller;

import com.tallerwebi.components.InputField;
import com.tallerwebi.model.DatosSetting;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioSetting;
import com.tallerwebi.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final ServicioSetting servicioSetting;
    private final SessionUtil sessionUtil;

    public SettingsController(ServicioSetting servicioSetting, SessionUtil sessionUtil) {
        this.servicioSetting = servicioSetting;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping()
    public ModelAndView cargarSettings(HttpServletRequest request) {

        Usuario usuarioLogueado = this.sessionUtil.getUsuarioLogueado(request);
        List<InputField> inputFields = this.servicioSetting.obtenerInputsForm(usuarioLogueado);

        ModelMap modelMap = new ModelMap();

        modelMap.addAttribute("inputFields", inputFields);
        modelMap.addAttribute("usuarioLogueado", usuarioLogueado);

        return new ModelAndView("settings", modelMap);
    }

    @PostMapping("/update")
    public String actualizarUsuario(@ModelAttribute("datosUpdate") DatosSetting datosSetting, HttpServletRequest request) {

        Usuario usuarioLogueado = this.sessionUtil.getUsuarioLogueado(request);

        this.servicioSetting.actualizarUsuario(usuarioLogueado.getEmail(), datosSetting);

        this.sessionUtil.setUsuarioEnSession(request, servicioSetting.obtenerUsuarioPorId(usuarioLogueado.getId()));

        return ("redirect:/settings");
    }
}

