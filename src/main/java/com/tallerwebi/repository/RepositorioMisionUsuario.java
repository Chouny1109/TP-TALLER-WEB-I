package com.tallerwebi.repository;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RepositorioMisionUsuario {
    void borrarMisionesDelUsuario(Usuario usuario);

    void saveAll(List<UsuarioMision> misiones);

    List<Mision> obtenerMisionesDelUsuarioPorId(Long id);

    Set<Long> obtenerElIdDeTodosLosUsuariosConMisionesAsignadas(LocalDate fechaActual);
}
