package com.tallerwebi.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    public Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        String nombreUsuario = (String) attributes.get("user");
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            nombreUsuario = "unknownUser_" + UUID.randomUUID();
        } else {
            nombreUsuario = nombreUsuario.trim();
        }
        System.out.println("HandshakeHandler: usuario asignado = " + nombreUsuario);

        return new StompPrincipal(nombreUsuario);
    }

}
