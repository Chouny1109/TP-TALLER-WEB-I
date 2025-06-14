package com.tallerwebi.integracion.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.user.SimpUserRegistry;

@Configuration
public class MockSimpUserRegistryConfig {
    @Bean
    @Primary
    public SimpUserRegistry simpUserRegistry() {
        return Mockito.mock(SimpUserRegistry.class);
    }
}
