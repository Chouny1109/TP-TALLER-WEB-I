package com.tallerwebi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TestController {
    @GetMapping("/testlogin")
    public String testLogin() {
        return "login"; // debe mostrar login.html sin problemas
    }
}

