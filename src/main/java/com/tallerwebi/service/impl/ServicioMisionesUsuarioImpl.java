package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.enums.ROL_USUARIO;
import com.tallerwebi.dominio.enums.TIPO_MISION;
import com.tallerwebi.dominio.excepcion.UsuarioNoAutenticadoException;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.model.UsuarioMisionDTO;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioMisiones;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.strategys.Mision.EstrategiaMision;
import com.tallerwebi.strategys.Mision.MisionFactory;
import com.tallerwebi.util.SessionUtil;
import com.tallerwebi.util.ValidadorMision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServicioMisionesUsuarioImpl implements ServicioMisionesUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioMisionUsuario repositorioMisionUsuario;
    private final RepositorioMisiones repositorioMisiones;
    private final SessionUtil sessionUtil;
    private final MisionFactory misionFactory;
    private final ValidadorMision validadorMision;

    public ServicioMisionesUsuarioImpl(RepositorioUsuario repositorioUsuario,
                                       RepositorioMisionUsuario repositorioMisionUsuario,
                                       RepositorioMisiones repositorioMisiones,
                                       SessionUtil sessionUtil,
                                       MisionFactory misionFactory, ValidadorMision validadorMision) {

        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
        this.repositorioMisiones = repositorioMisiones;
        this.sessionUtil = sessionUtil;
        this.misionFactory = misionFactory;
        this.validadorMision = validadorMision;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioMisionDTO> obtenerLasMisionesDelUsuarioPorId(Long id, LocalDate fecha) throws UsuarioNoExistente {
        Usuario usuario = this.repositorioUsuario.buscarUsuarioPorId(id);

        if (usuario == null) {
            throw new UsuarioNoExistente();
        }

        List<UsuarioMision> usuarioMisiones = this.repositorioMisionUsuario.obtenerMisionesDelUsuarioPorId(id, fecha);

        return usuarioMisiones.stream().map(m ->
                        new UsuarioMisionDTO(
                                m.getId(),
                                m.getMision().getDescripcion(),
                                m.getProgreso(),
                                m.getMision().getCantidad(),
                                m.getMision().getExperiencia(),
                                m.getMision().getCopas(),
                                m.getCompletada(),
                                m.getCanjeada()
                        ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void guardarRelacionesUsuarioMision(List<UsuarioMision> relaciones) {
        this.repositorioMisionUsuario.saveAll(relaciones);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Mision> obtenerMisiones() {
        return this.repositorioMisiones.obtenerMisiones();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> obtenerUsuarios() {
        return this.repositorioUsuario.obtenerUsuarios();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Long> obtenerLosIDdeTodosLosUsuariosConMisionesAsignadas() {
        return this.repositorioMisionUsuario.obtenerElIdDeTodosLosUsuariosConMisionesAsignadas(LocalDate.now());
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
    public List<UsuarioMision> crearRelacionUsuarioMision(Usuario usuario, List<Mision> misiones) {
        return misiones.stream().map(mision ->
                crearRelacionUsuarioMision(usuario, mision)).collect(Collectors.toList());
    }

    @Override
    public UsuarioMision crearRelacionUsuarioMision(Usuario usuario, Mision mision) {
        return new UsuarioMision(usuario, mision);
    }

    @Override
    public void completarMisiones(HttpServletRequest request) {
        Usuario logueado = sessionUtil.getUsuarioLogueado(request);

        if (logueado != null) {

            List<UsuarioMision> usuarioMision = logueado.getMisiones();

            usuarioMision.forEach((mision) -> {
                if (!mision.getCompletada()) {
                    TIPO_MISION tipo = mision.getMision().getTipoMision().getNombre();
                    EstrategiaMision estrategiaMision = misionFactory.obtenerEstrategia(tipo);
                    estrategiaMision.completarMision(logueado, mision);
                }
            });
        }
    }

    @Override
    @Transactional
    public void cambiarMision(Usuario logueado, Long idMision) {
        Usuario usuarioBd = repositorioUsuario.buscarUsuarioPorId(logueado.getId());
        UsuarioMision usuarioMision = this.repositorioMisionUsuario.obtenerUsuarioMision(idMision);

        this.validadorMision.validarMision(usuarioBd, usuarioMision);

        usuarioBd.setMonedas(usuarioBd.getMonedas() - ValidadorMision.COSTO_MISION);
        Mision nuevaMision = this.repositorioMisionUsuario.obtenerMisionNoAsignadaParaUsuario(usuarioBd.getId(), LocalDate.now());
        this.repositorioMisionUsuario.save(crearRelacionUsuarioMision(usuarioBd, nuevaMision));

        borrarRelacionUsuarioMision(usuarioMision);
        actualizarUsuario(usuarioBd);
    }

    @Transactional
    @Override
    public void actualizarUsuario(Usuario usuario) {
        this.repositorioUsuario.modificar(usuario);
    }

    @Transactional
    @Override
    public void borrarRelacionUsuarioMision(UsuarioMision usuarioMision) {
        this.repositorioMisionUsuario.delete(usuarioMision);
    }

    @Transactional
    @Override
    public void asignarMisionesAUsuario(Usuario usuario) {
        Set<Long> usuariosConMisiones = this.repositorioMisionUsuario.
                obtenerElIdDeTodosLosUsuariosConMisionesAsignadas(LocalDate.now());
        boolean tieneMisionesAsignadas = tieneMisionesAsignadas(usuario, usuariosConMisiones);

        if (!tieneMisionesAsignadas && ROL_USUARIO.JUGADOR.equals(usuario.getRol())) {
            List<Mision> misionesUsuario = obtenerMisionesAleatorias(this.repositorioMisiones.obtenerMisiones());
            this.repositorioMisionUsuario.saveAll(crearRelacionUsuarioMision(usuario, misionesUsuario));
        }
    }
}
