package com.tallerwebi.integracion.config;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class MockSimpMessagingTemplateConfig {
    @Bean
    @Primary
    public SimpMessagingTemplate simpMessagingTemplate() {
        return Mockito.mock(SimpMessagingTemplate.class);
    }
}

