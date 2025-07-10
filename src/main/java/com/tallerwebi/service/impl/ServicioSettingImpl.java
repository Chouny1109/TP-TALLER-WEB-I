package com.tallerwebi.service.impl;

import com.tallerwebi.components.InputField;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.DatosSetting;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioSetting;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class ServicioSettingImpl implements ServicioSetting {

    private final RepositorioUsuario repositorioUsuario;

    public ServicioSettingImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public List<InputField> obtenerInputsForm(Usuario usuarioLogueado) {

        return new ArrayList<>(Arrays.asList(
                new InputField("text", "userName", "Usuario", usuarioLogueado.getNombreUsuario()),
                new InputField("email", "email", "Email", usuarioLogueado.getEmail()),
                new InputField("password", "password", "Contraseña", null)
        ));
    }

    @Override
    public void actualizarUsuario(String email, DatosSetting datosSetting) {
        Usuario usuario = this.repositorioUsuario.buscar(email);

        if (usuario != null) {
            usuario.setNombreUsuario(datosSetting.getUserName());
            usuario.setPassword(datosSetting.getPassword());
            usuario.setEmail(datosSetting.getEmail());

            this.repositorioUsuario.modificar(usuario);
        }
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return this.repositorioUsuario.buscarUsuarioPorId(id);
    }
}
