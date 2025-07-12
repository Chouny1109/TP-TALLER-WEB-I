package com.tallerwebi.service;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.model.UsuarioMisionDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ServicioMisionesUsuario {
    List<UsuarioMisionDTO> obtenerLasMisionesDelUsuarioPorId(Long id, LocalDate fecha) throws UsuarioNoExistente;

    void actualizarUsuario(Usuario usuario);

    void borrarRelacionUsuarioMision(UsuarioMision usuarioMision);

    void asignarMisionesAUsuario(Usuario usuario);

    void guardarRelacionesUsuarioMision(List<UsuarioMision> relaciones);

    List<Mision> obtenerMisiones();

    List<Usuario> obtenerUsuarios();

    Set<Long> obtenerLosIDdeTodosLosUsuariosConMisionesAsignadas();

    Boolean tieneMisionesAsignadas(Usuario usuario, Set<Long> usuariosConMisionesAsignadas);

    List<Mision> obtenerMisionesAleatorias(List<Mision> misionesBd);

    List<UsuarioMision> crearRelacionUsuarioMision(Usuario usuario, List<Mision> misiones);

    UsuarioMision crearRelacionUsuarioMision(Usuario usuario, Mision mision);

    void completarMisiones(HttpServletRequest request) throws UsuarioNoExistente;

    void cambiarMision(Usuario logueado, Long idMision);
}
