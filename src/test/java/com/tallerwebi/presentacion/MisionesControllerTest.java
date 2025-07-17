package com.tallerwebi.presentacion;

import com.tallerwebi.controller.MisionesController;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMisionDTO;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.util.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class MisionesControllerTest {

    private ServicioMisionesUsuario servicioMisiones;
    private SessionUtil sessionUtil;
    private HttpServletRequest request;
    private MisionesController misionesController;
    private final List<UsuarioMisionDTO> misionesMock = List.of(new UsuarioMisionDTO(1L, "Mision 1", 0, 5, 500, 100, false, false));


    @BeforeEach
    public void setUp() {
        servicioMisiones = mock(ServicioMisionesUsuario.class);
        sessionUtil = mock(SessionUtil.class);
        request = mock(HttpServletRequest.class);
//        misionesController = new MisionesController(servicioMisiones, sessionUtil);
        misionesController = new MisionesController(servicioMisiones);
    }

    @Test
    public void caundoCargoLaVistaObtenerMisionesMeDevuelveUnModelAndView() throws UsuarioNoExistente {
        Usuario logueado = givenDadoUnUsuarioLogueadoConObtenerMisiones();
        String vista = whenCargoLaVista();
        thenLaVistaMeDevueleveUnModelAnView(vista, logueado);
    }

    private Usuario givenDadoUnUsuarioLogueadoConObtenerMisiones() throws UsuarioNoExistente {
        Usuario logueado = new Usuario();
        logueado.setId(1L);

        when(sessionUtil.getUsuarioLogueado(request)).thenReturn(logueado);
        when(servicioMisiones.obtenerLasMisionesDelUsuarioPorId(logueado.getId(), LocalDate.now())).thenReturn(misionesMock);

        return logueado;
    }

    private String whenCargoLaVista() throws UsuarioNoExistente {
        return "misiones";
    }

    private void thenLaVistaMeDevueleveUnModelAnView(String vista, Usuario logueado) {
        assertThat(vista, equalToIgnoringCase("misiones"));
    }

}
