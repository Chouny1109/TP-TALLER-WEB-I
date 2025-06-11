package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioMisiones;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioMisionesUsuario;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ServicioMisionesUsuarioImpl implements ServicioMisionesUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioMisiones repositorioMisiones;
    private final RepositorioMisionUsuario repositorioMisionUsuario;

    public ServicioMisionesUsuarioImpl(RepositorioUsuario repositorioUsuario, RepositorioMisiones repositorioMisiones, RepositorioMisionUsuario repositorioMisionUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMisiones = repositorioMisiones;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
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
        List<Mision> misionesBd = this.repositorioMisiones.obtenerMisiones();

        for (Usuario usuario : usuariosBd) {
            try {
                borrarMisionesDelUsuario(usuario);
                relaciones.addAll(
                        asignarMisionesAUsuario(usuario, misionesBd)
                );
            } catch (UsuarioNoExistente e) {
                System.out.println("No se encontró el usuario");
            }
        }
        repositorioMisionUsuario.saveAll(relaciones);
    }

    @Override
    public List<Mision> generarMisionesAleatorias() {
        List<Mision> misionesBd = repositorioMisiones.obtenerMisiones();
        List<Mision> misionesAleatorias = new ArrayList<>(misionesBd);
        Collections.shuffle(misionesAleatorias);
        return misionesAleatorias.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * Borra las misiones del usuario solicitado
     *
     * @param usuario Usuario al cual se desea borrarle o reiniciarles las misiones.
     * @throws UsuarioNoExistente Si no existe ese usuario en la base de datos
     */
    @Override
    public void borrarMisionesDelUsuario(Usuario usuario) throws UsuarioNoExistente {
        Usuario buscado = repositorioUsuario.buscarUsuarioPorId(usuario.getId());

        if (buscado == null) {
            throw new UsuarioNoExistente();
        }
        repositorioMisionUsuario.borrarMisionesDelUsuario(buscado);
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
