package com.tallerwebi.controller;

import com.tallerwebi.service.ServicioAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ServicioAdmin servicioAdmin;

    public AdminController(ServicioAdmin servicioAdmin) {
        this.servicioAdmin = servicioAdmin;
    }


}
