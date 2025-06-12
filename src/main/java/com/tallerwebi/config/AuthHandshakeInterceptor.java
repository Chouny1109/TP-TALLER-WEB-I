package com.tallerwebi.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tallerwebi.model.Usuario;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
            HttpSession session = httpRequest.getSession(false);

            if (session != null) {
                Object usuario = session.getAttribute("USUARIO");
                if (usuario instanceof Usuario) {
                    String nombreUsuario = ((Usuario) usuario).getNombreUsuario();
                    attributes.put("user", nombreUsuario);
                    System.out.println("Handshake - Usuario conectado: " + nombreUsuario);
                } else {
                    System.out.println("Handshake - No se encontró un usuario válido en la sesión");
                }
            } else {
                System.out.println("Handshake - No hay sesión HTTP");
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // No hace nada
    }
}


