package com.tallerwebi.presentacion;

import com.tallerwebi.controller.MisionesController;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMisionDTO;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.util.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
    private final List<UsuarioMisionDTO> misionesMock = List.of(new UsuarioMisionDTO("Mision 1", 0, 5, 500, 100, false, false));

    @BeforeEach
    public void setUp() {
        servicioMisiones = mock(ServicioMisionesUsuario.class);
        sessionUtil = mock(SessionUtil.class);
        request = mock(HttpServletRequest.class);
        misionesController = new MisionesController(servicioMisiones, sessionUtil);
    }

    @Test
    public void caundoCargoLaVistaObtenerMisionesMeDevuelveUnModelAndView() throws UsuarioNoExistente {
        Usuario logueado = givenDadoUnUsuarioLogueadoConObtenerMisiones();
        ModelAndView mav = whenCargoLaVista(request);
        thenLaVistaMeDevueleveUnModelAnView(mav, logueado);
    }

    private Usuario givenDadoUnUsuarioLogueadoConObtenerMisiones() throws UsuarioNoExistente {
        Usuario logueado = new Usuario();
        logueado.setId(1L);

        when(sessionUtil.getUsuarioLogueado(request)).thenReturn(logueado);
        when(servicioMisiones.obtenerLasMisionesDelUsuarioPorId(logueado.getId())).thenReturn(misionesMock);

        return logueado;
    }

    private ModelAndView whenCargoLaVista(HttpServletRequest request) throws UsuarioNoExistente {
        return misionesController.obtenerMisiones(request);
    }

    private void thenLaVistaMeDevueleveUnModelAnView(ModelAndView mav, Usuario logueado) {
        assertThat(mav.getViewName(), equalToIgnoringCase("misiones"));
        assertThat(mav.getModel().get("misiones"), equalTo(misionesMock));
    }

}
