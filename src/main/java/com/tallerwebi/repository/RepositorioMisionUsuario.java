package com.tallerwebi.repository;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;

import java.util.List;

public interface RepositorioMisionUsuario {
    void borrarMisionesDelUsuario(Usuario usuario);

    void saveAll(List<UsuarioMision> misiones);

    List<Mision> obtenerMisionesDelUsuarioPorId(Long id);
}
