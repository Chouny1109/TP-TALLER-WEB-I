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

    List<UsuarioMision> obtenerMisionesDelUsuarioPorId(Long id, LocalDate fecha);

    Set<Long> obtenerElIdDeTodosLosUsuariosConMisionesAsignadas(LocalDate fechaActual);

    void save(UsuarioMision usuarioMision);

    UsuarioMision obtenerUsuarioMision(Long idMision);

    void delete(UsuarioMision usuarioMision);

    Mision obtenerMisionNoAsignadaParaUsuario(Long usuarioId, LocalDate fecha);
}
