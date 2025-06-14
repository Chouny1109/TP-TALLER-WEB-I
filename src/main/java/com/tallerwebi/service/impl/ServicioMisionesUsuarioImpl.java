package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioMisiones;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.repository.impl.RepositorioMisionesImpl;
import com.tallerwebi.service.ServicioMisionesUsuario;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class ServicioMisionesUsuarioImpl implements ServicioMisionesUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioMisionUsuario repositorioMisionUsuario;
    private final RepositorioMisiones repositorioMisiones;

    public ServicioMisionesUsuarioImpl(RepositorioUsuario repositorioUsuario, RepositorioMisionUsuario repositorioMisionUsuario, RepositorioMisiones repositorioMisiones) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
        this.repositorioMisiones = repositorioMisiones;
    }

    /**
     * @param id ID del usuario cuyas misiones se quieren obtener
     * @return una lista de objetos {@link UsuarioMision} perteneciente al usuario buscado
     * @throws UsuarioNoExistente si no existe el usuario buscado en la base de datos
     */
    @Override
    public List<Mision> obtenerLasMisionesDelUsuario(Long id) throws UsuarioNoExistente {
        Usuario buscado = repositorioUsuario.buscarUsuarioPorId(id);

        if (buscado == null) {
            throw new UsuarioNoExistente();
        }

        return this.repositorioMisionUsuario.obtenerMisionesDelUsuarioPorId(id);
    }

    /**
     * Asigna aleatoriamente tres misiones diarias a cada usuario del sistema
     * <p>
     * 1) Primero borra todas misiones del usuario antes de asignar nuevas.
     * <p>
     * 2) Mezcla aleatoriamente el orden de las misiones y de estas saca una lista de tres elementos
     * <p>
     * 3) Por cada mision creo un objeto del tipo {@link UsuarioMision} y lo guarda
     * <p>
     * 4) Guarda en la base de datos
     *
     * @throws UsuarioNoExistente si no existe un usuario para asignarle misiones
     */

   @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void asignarMisionesDiarias() {
        List<Usuario> usuariosBd = repositorioUsuario.obtenerUsuarios();
        List<UsuarioMision> relaciones = new ArrayList<>();
        Set<Long> usuariosConMisionesAsignadas = this.repositorioMisionUsuario.
                obtenerElIdDeTodosLosUsuariosConMisionesAsignadas(LocalDate.now());
        List<Mision> misionesBd = this.repositorioMisiones.obtenerMisiones();

        for (Usuario usuario : usuariosBd) {
            if (!tieneMisionesAsignadas(usuario, usuariosConMisionesAsignadas)) {

                List<Mision> misionesAleatorias = this.obtenerMisionesAleatorias(misionesBd);
                relaciones.addAll(
                        misionesAleatorias.stream().map(
                                element -> new UsuarioMision(usuario, element)
                        ).collect(Collectors.toList())
                );
            }
        }
        repositorioMisionUsuario.saveAll(relaciones);
    }

    @Override
    public Boolean tieneMisionesAsignadas(Usuario usuario, Set<Long> usuariosConMisionesAsignadas) {
        return usuariosConMisionesAsignadas.contains(usuario.getId());
    }

    @Override
    public List<Mision> obtenerMisionesAleatorias(List<Mision> misionesBd) {
        if (misionesBd == null || misionesBd.isEmpty()) {
            return Collections.emptyList();
        }

        List<Mision> misionesAleatorias = new ArrayList<>(misionesBd);
        Collections.shuffle(misionesAleatorias);
        return misionesAleatorias.stream().limit(3).collect(Collectors.toList());
    }

    @Override
    public List<UsuarioMision> asignarMisionesAUsuario(Usuario usuario, List<Mision> misiones) {
        List<Mision> misionesUsuario = new ArrayList<>(misiones);

        Collections.shuffle(misionesUsuario);

        return misionesUsuario.stream().limit(3).map(element ->
                        new UsuarioMision(usuario, element)).
                collect(Collectors.toList());
    }
}
