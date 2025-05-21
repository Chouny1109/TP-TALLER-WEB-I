package com.tallerwebi.util;

import com.tallerwebi.model.Usuario;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionUtil {

    private static final String USUARIO_SESSION_KEY = "USUARIO";

    public Usuario getUsuarioLogueado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            return (Usuario) session.getAttribute(USUARIO_SESSION_KEY);
        }
        return null;
    }

    public void setUsuarioEnSession(HttpServletRequest request, Usuario usuario) {
        request.getSession(true).setAttribute(USUARIO_SESSION_KEY, usuario);
    }

    public void cerrarSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(USUARIO_SESSION_KEY);
        }
    }
}
