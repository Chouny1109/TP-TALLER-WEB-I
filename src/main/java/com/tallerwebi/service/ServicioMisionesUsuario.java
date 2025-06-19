package com.tallerwebi.service;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface ServicioMisionesUsuario {
    List<Mision> obtenerLasMisionesDelUsuarioPorId(Long id) throws UsuarioNoExistente;

    void asignarMisionesDiarias() throws UsuarioNoExistente;

    void asignarMisionesAUsuario(Usuario usuario);

    Boolean tieneMisionesAsignadas(Usuario usuario, Set<Long> usuariosConMisionesAsignadas);

    List<Mision> obtenerMisionesAleatorias(List<Mision> misionesBd);

    List<UsuarioMision> crearRelacionUsuarioMision(Usuario usuario, List<Mision> misiones);

    void completarMisiones(HttpServletRequest request) throws UsuarioNoExistente;

}
