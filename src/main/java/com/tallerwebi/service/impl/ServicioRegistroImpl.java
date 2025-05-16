package com.tallerwebi.service.impl;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioRegistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioRegistro")
@Transactional
public class ServicioRegistroImpl implements ServicioRegistro {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioRegistroImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }


    @Override
    public Boolean registrar(Usuario usuario, String confirmarPassword) throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());

        if (!usuario.getEmail().contains("@") || !usuario.getEmail().contains(".com")) {
            throw new EmailInvalido();
        }
        if (!usuario.getPassword().equals(confirmarPassword)) {
            throw new PasswordsNotEquals();
        }
        if (usuarioEncontrado != null) {
            throw new UsuarioExistente();
        }

        return repositorioUsuario.guardar(usuario);
    }
}
