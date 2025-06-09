package com.tallerwebi.integracion.config;

import com.tallerwebi.service.ServicioRecovery;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockServicioRecoveryConfig {

    @Bean
    public ServicioRecovery servicioRecovery() {
        return Mockito.mock(ServicioRecovery.class);
    }
}
