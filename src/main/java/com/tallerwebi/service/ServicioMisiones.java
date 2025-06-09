package com.tallerwebi.service;


import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;

import java.util.List;

public interface ServicioMisiones {
    List<Mision> obtenerLasMisionesDelUsuario(Long id) throws UsuarioNoExistente;

    void asignarMisionesDiarias();
}
