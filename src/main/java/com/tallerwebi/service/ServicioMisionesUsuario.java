package com.tallerwebi.service;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;

import java.util.List;

public interface ServicioMisionesUsuario {
    List<Mision> obtenerLasMisionesDelUsuario(Long id) throws UsuarioNoExistente;

    void asignarMisionesDiarias() throws UsuarioNoExistente;

    void borrarMisionesDelUsuario(Usuario usuario) throws UsuarioNoExistente;

    List<UsuarioMision> asignarMisionesAUsuario(Usuario usuario, List<Mision> misiones);

    List<Mision> generarMisionesAleatorias();
}
