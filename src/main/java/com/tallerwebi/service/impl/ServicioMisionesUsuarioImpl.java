package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.enums.TIPO_MISION;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioMisiones;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.strategys.Mision.EstrategiaMision;
import com.tallerwebi.strategys.Mision.MisionFactory;
import com.tallerwebi.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private final SessionUtil sessionUtil;
    private final MisionFactory misionFactory;

    public ServicioMisionesUsuarioImpl(RepositorioUsuario repositorioUsuario,
                                       RepositorioMisionUsuario repositorioMisionUsuario,
                                       RepositorioMisiones repositorioMisiones,
                                       SessionUtil sessionUtil,
                                       MisionFactory misionFactory) {

        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMisionUsuario = repositorioMisionUsuario;
        this.repositorioMisiones = repositorioMisiones;
        this.sessionUtil = sessionUtil;
        this.misionFactory = misionFactory;
    }

    @Override
    public List<Mision> obtenerLasMisionesDelUsuarioPorId(Long id) throws UsuarioNoExistente {
        Usuario buscado = repositorioUsuario.buscarUsuarioPorId(id);

        if (buscado == null) {
            throw new UsuarioNoExistente();
        }

        return this.repositorioMisionUsuario.obtenerMisionesDelUsuarioPorId(id);
    }

    @Scheduled(cron = "0 0/3 * * * *")
    @Override
    public void asignarMisionesDiarias() {
        List<Usuario> usuariosBd = this.repositorioUsuario.obtenerUsuarios();
        List<UsuarioMision> relaciones = new ArrayList<>();
        Set<Long> usuariosConMisionesAsignadas = this.repositorioMisionUsuario.
                obtenerElIdDeTodosLosUsuariosConMisionesAsignadas(LocalDate.now());
        List<Mision> misionesBd = this.repositorioMisiones.obtenerMisiones();

        for (Usuario usuario : usuariosBd) {
            if (!tieneMisionesAsignadas(usuario, usuariosConMisionesAsignadas)) {
                List<Mision> misionesAleatorias = this.obtenerMisionesAleatorias(misionesBd);
                relaciones.addAll(crearRelacionUsuarioMision(usuario, misionesAleatorias));
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
    public List<UsuarioMision> crearRelacionUsuarioMision(Usuario usuario, List<Mision> misiones) {
        return misiones.stream().map(element -> new UsuarioMision(usuario, element)).collect(Collectors.toList());
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
    public void asignarMisionesAUsuario(Usuario usuario) {
        List<Mision> misionesUsuario = obtenerMisionesAleatorias(this.repositorioMisiones.obtenerMisiones());
        this.repositorioMisionUsuario.saveAll(crearRelacionUsuarioMision(usuario, misionesUsuario));
    }
}
