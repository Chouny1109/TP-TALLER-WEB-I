package com.tallerwebi.controller.rest;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioDTO;
import com.tallerwebi.model.UsuarioMisionDTO;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/misiones")
public class MisionesRestController {

    private final SessionUtil session;
    private final ServicioMisionesUsuario servicio;
    private static final Logger LOGGER = LoggerFactory.getLogger(MisionesRestController.class);

    @Autowired
    public MisionesRestController(SessionUtil session, ServicioMisionesUsuario servicio) {
        this.session = session;
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<?> obtenerMisiones(HttpServletRequest request) {
        try {
            Usuario logueado = session.getUsuarioLogueado(request);

            if (logueado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        Map.of(
                                "error", "No se ha podido obtener el usuario"
                        )
                );
            }

            LocalDate fechaActual = LocalDate.now();
            List<UsuarioMisionDTO> misionesDelUsuario = servicio.obtenerLasMisionesDelUsuarioPorId(
                    logueado.getId(), fechaActual);

            if (misionesDelUsuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of(
                                "error", "No se han encontrado misiones para el usuario"
                        )
                );
            }

            return ResponseEntity.ok(misionesDelUsuario);

        } catch (Exception e) {
            LOGGER.error("Error al obtener las misiones del usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "error", "Error al obtener las misiones del usuario."
                    )
            );
        }
    }
}
