package com.tallerwebi.presentacion;

import com.tallerwebi.components.InputField;
import com.tallerwebi.dominio.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private static final Usuario usuario = new Usuario("ezkluci","eluci@gmail","ezequiel1234");

    @GetMapping
    public String cargarSettings(Model model) {
        List<InputField>inputsSetting = new ArrayList<>();
        inputsSetting.add(new InputField("text","userName","Usuario",usuario.getNombreUsuario(),false));
        inputsSetting.add(new InputField("email","email","Email",usuario.getEmail(),false));
        inputsSetting.add(new InputField("password","password","Password",usuario.getPassword(),false));

        model.addAttribute("usuario", usuario);
        model.addAttribute("inputsSetting", inputsSetting);
        return "settings";
    }

    @PostMapping("actualizar-usuario")
    public String actualizarUsuario(@ModelAttribute Usuario usuario){
        return "redirect:/home";
    }
}

