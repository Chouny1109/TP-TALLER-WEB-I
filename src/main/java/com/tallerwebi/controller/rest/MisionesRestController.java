package com.tallerwebi.controller.rest;

import com.tallerwebi.dominio.excepcion.UsuarioNoAutenticadoException;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMisionDTO;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MisionesRestController {

    private final SessionUtil session;
    private final ServicioMisionesUsuario servicio;
    private static final Logger LOGGER = LoggerFactory.getLogger(MisionesRestController.class);

    @Autowired
    public MisionesRestController(SessionUtil session, ServicioMisionesUsuario servicio) {
        this.session = session;
        this.servicio = servicio;
    }

    @GetMapping("/misiones")
    public ResponseEntity<?> obtenerMisiones(HttpServletRequest request) {
        Usuario logueado = session.getUsuarioLogueado(request);

        if (logueado == null) {
            throw new UsuarioNoAutenticadoException("El usuario no se encuentra autenticado");
        }

        List<UsuarioMisionDTO> misionesDelUsuario = servicio.obtenerLasMisionesDelUsuarioPorId(logueado.getId(), LocalDate.now());

        return ResponseEntity.ok(misionesDelUsuario);
    }

    @PutMapping("/misiones/{id}")
    public ResponseEntity<?> cambiarMision(@PathVariable Long id, HttpServletRequest request) {
        Usuario logueado = session.getUsuarioLogueado(request);

        if (logueado == null) {
            throw new UsuarioNoAutenticadoException("El usuario no se encuentra autenticado");
        }
        servicio.cambiarMision(logueado, id);
        return ResponseEntity.ok().build();
    }
}
