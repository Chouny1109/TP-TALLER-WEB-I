package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/avatar")
public class AvatarController {



    @GetMapping
    public String cargarAvatar() {

        return "avatar";


    }
    @PostMapping
    public String actualizarAvatar(Model model) {

        return "redirect:/tienda";

    }

}
