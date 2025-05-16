package com.tallerwebi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recovery")
public class RecoveryController {

    @GetMapping
    public String cargarRecovery() {
        return "recovery";
    }

}
