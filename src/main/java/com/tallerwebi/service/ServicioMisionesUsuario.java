package com.tallerwebi.service;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;

import java.util.List;
import java.util.Set;

public interface ServicioMisionesUsuario {
    List<Mision> obtenerLasMisionesDelUsuarioPorId(Long id) throws UsuarioNoExistente;

    void asignarMisionesDiarias() throws UsuarioNoExistente;

    void asignarMisionesAUsuario(Usuario usuario);

    Boolean tieneMisionesAsignadas(Usuario usuario, Set<Long> usuariosConMisionesAsignadas);

    List<Mision> obtenerMisionesAleatorias(List<Mision> misionesBd);

    List<UsuarioMision> crearRelacionUsuarioMision(Usuario usuario, List<Mision> misiones);

    //  QUE DATOS TENDRIAMOS ????

    //  USUARIO LOGUEADO O USUARIOS DE LA BD - MISIONES DE ESE USUARIO

    //  Tendria un usuario logueado
    //  Ese usuario logueado tiene misiones
    //  cada mision ejecuta un metodo distinto segun su tipo
    //  ese metodo alteraria el estado de completado
    //  habria que guardar la mision nuevamente ya que actualizamos un estado

    //  RESTRICCIONES
    //-----------------
    //  1- QUE PASA SI LA MISION DICE "GANAR 5 PARTIDAS" Y OTRA MISION "GANAR 3 PARTIDAS"
    //  --> AMBAS SERIAN DEL TIPO [GANAR_PARTIDA] PERO SU EJECUCION SERIA DISTINTA
    //  --> UNA TIENE QUE VALIDAR QUE HAYA GANADO 5 PARTIDAS Y OTRA 3 PARTIDAS

  /*
        CUANDO SE EJECUTAN LOS METODOS ?
        --------------------------------

        * Ejecutamos el metodo manualmente cada vez que haga una accion distinta ?




   */
}
