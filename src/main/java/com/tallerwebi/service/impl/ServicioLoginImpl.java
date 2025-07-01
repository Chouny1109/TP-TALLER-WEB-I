package com.tallerwebi.service.impl;




import com.tallerwebi.dominio.excepcion.UsuarioExistente;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.repositorioUsuario = repositorioUsuario;
    }
    @PostConstruct
    public void init() {
        System.out.println("Login: PasswordEncoder inyectado: " + (passwordEncoder != null));
        System.out.println("Login: RepositorioUsuario inyectado: " + (repositorioUsuario != null));
    }


    @Override
    public Usuario consultarUsuario (String email, String password) {

        Usuario usuario = repositorioUsuario.buscar(email);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario;
        }
        return null;


    }

    @Override
    public void cerrarSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

