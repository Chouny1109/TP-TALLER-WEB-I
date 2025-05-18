package com.tallerwebi.service.impl;

import com.tallerwebi.components.InputField;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioSetting;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ServicioSettingImpl implements ServicioSetting {
    @Override
    public List<InputField> obtenerInputsForm(Usuario usuarioLogueado) {
        return new ArrayList<>(Arrays.asList(
                new InputField("text","userName","Usuario",usuarioLogueado.getNombreUsuario(),true),
                new InputField("email","email","Email",usuarioLogueado.getEmail(),true),
                new InputField("password","password","Contraseña",usuarioLogueado.getPassword(),true)
                ));
    }
}
