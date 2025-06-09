package com.tallerwebi.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = {
        "com.tallerwebi.service",   // donde tenés lógica de negocio
        "com.tallerwebi.dominio",   // donde podrían estar los beans relacionados al mail
        "com.tallerwebi.repository",
        "com.tallerwebi.config"     // para que detecte MailDevConfig, MailProdConfig, MailTestConfig
})
public class RootConfig {
}
