package com.tallerwebi.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mercadopago.MercadoPagoConfig;

@Component
public class MercadoPagoConfigInit {

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken("APP_USR-2976850985309326-062410-9fc41a37c4ec907cf594ff63dd8c618f-2517029922");
    }
}
