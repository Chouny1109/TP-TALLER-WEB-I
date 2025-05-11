package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/misiones")
public class MisionesController {

    private static final List<String> misiones = new ArrayList<>(Arrays.asList(
             ("Mision 1"),
             ("Mision 2"),
             ("Mision 3"),
             ("Mision 4")
    ));

    //Manera moderna de mandar datos a  la vista
    @GetMapping()
    public String misiones(Model model) {
        //agrego los atributos al map
        model.addAttribute("misiones", misiones);
        return ("misiones");
    }
}
