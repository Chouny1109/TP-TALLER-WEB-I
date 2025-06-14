package com.tallerwebi.service;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ServicioMisionesUsuario {
    List<Mision> obtenerLasMisionesDelUsuario(Long id) throws UsuarioNoExistente;

    void asignarMisionesDiarias() throws UsuarioNoExistente;

    List<UsuarioMision> asignarMisionesAUsuario(Usuario usuario, List<Mision> misiones);

    Boolean tieneMisionesAsignadas(Usuario usuario, Set<Long> usuariosConMisionesAsignadas);

    List<Mision> obtenerMisionesAleatorias(List<Mision> misionesBd);
}
