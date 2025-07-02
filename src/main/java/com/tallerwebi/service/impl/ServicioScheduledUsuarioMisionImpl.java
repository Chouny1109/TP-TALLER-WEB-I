package com.tallerwebi.service.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.service.ServicioScheduledMisionUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ServicioScheduledUsuarioMisionImpl implements ServicioScheduledMisionUsuario {

    private final ServicioMisionesUsuario servicioMisionesUsuario;

    @Autowired
    public ServicioScheduledUsuarioMisionImpl(ServicioMisionesUsuario servicioMisionesUsuario) {
        this.servicioMisionesUsuario = servicioMisionesUsuario;
        System.out.println("🔥 Instancia creada: " + this.hashCode());
    }

    @Scheduled(cron = "0 */1 * * * *")
    @Override
    public void asignarMisionesDiarias() {
        List<Usuario> usuariosBd = servicioMisionesUsuario.obtenerUsuarios();
        Set<Long> usuariosConMisionesAsignadas = servicioMisionesUsuario.obtenerLosIDdeTodosLosUsuariosConMisionesAsignadas();
        List<Mision> misionesBd = servicioMisionesUsuario.obtenerMisiones();
        List<UsuarioMision> relaciones = new ArrayList<>();

        for (Usuario usuario : usuariosBd) {
            if (!servicioMisionesUsuario.tieneMisionesAsignadas(usuario, usuariosConMisionesAsignadas)) {
                List<Mision> misionesAleatorias = servicioMisionesUsuario.obtenerMisionesAleatorias(misionesBd);
                relaciones.addAll(servicioMisionesUsuario.crearRelacionUsuarioMision(usuario, misionesAleatorias));
            }
        }

        this.servicioMisionesUsuario.guardarRelacionesUsuarioMision(relaciones);
    }
}
