package com.tallerwebi.service;

import com.tallerwebi.components.InputField;
import com.tallerwebi.model.DatosSetting;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface ServicioSetting {
    List<InputField> obtenerInputsForm(Usuario usuarioLogueado);
    void actualizarUsuario(String email,DatosSetting datosSetting);
    Usuario obtenerUsuarioPorId(Long id);
}
