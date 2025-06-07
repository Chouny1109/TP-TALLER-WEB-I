package com.tallerwebi.service.impl;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioRegistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioRegistroImpl implements ServicioRegistro {
    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServicioRegistroImpl(RepositorioUsuario repositorioUsuario) {

        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {
        System.out.println("Registro: PasswordEncoder inyectado: " + (passwordEncoder != null));
        System.out.println("Registro: RepositorioUsuario inyectado: " + (repositorioUsuario != null));
    }


    @Override
    public Boolean registrar(Usuario usuario, String confirmarPassword) throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        Usuario usuarioEncontrado = repositorioUsuario.buscar(usuario.getEmail());

        if (!usuario.getEmail().contains("@") || !usuario.getEmail().contains(".com")) {
            throw new EmailInvalido();
        }
        if (!usuario.getPassword().trim().equals(confirmarPassword.trim())) {
            throw new PasswordsNotEquals();
        }
        if (usuarioEncontrado != null) {
            throw new UsuarioExistente();
        }

        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);

        return repositorioUsuario.guardar(usuario);
    }
}
