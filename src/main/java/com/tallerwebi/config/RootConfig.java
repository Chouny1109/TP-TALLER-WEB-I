package com.tallerwebi.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = {
        "com.tallerwebi.service",
        "com.tallerwebi.repository",
        "com.tallerwebi.dominio",
        "com.tallerwebi.config", // si tenés mail config, etc.
        "com.tallerwebi.util",
        "com.tallerwebi.strategys "

})
public class RootConfig {
}
