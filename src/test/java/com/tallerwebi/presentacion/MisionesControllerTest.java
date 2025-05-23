package com.tallerwebi.presentacion;

import com.tallerwebi.controller.MisionesController;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioMisiones;
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

    private ServicioMisiones servicioMisiones;
    private SessionUtil sessionUtil;
    private HttpServletRequest request;
    private MisionesController misionesController;

    private final List<Mision> misionesMock = List.of(new Mision("Mision 1", "Descripcion"));


    @BeforeEach
    public void setUp() {
        servicioMisiones = mock(ServicioMisiones.class);
        sessionUtil = mock(SessionUtil.class);
        request = mock(HttpServletRequest.class);
        misionesController = new MisionesController(servicioMisiones, sessionUtil);
    }

    @Test
    public void caundoCargoLaVistaMisionesMeDevuelveUnModelAndView() {
        Usuario logueado = givenDadoUnUsuarioLogueadoConMisiones();
        ModelAndView mav = whenCargoLaVista(request);
        thenLaVistaMeDevueleveUnModelAnView(mav, logueado);
    }

    private Usuario givenDadoUnUsuarioLogueadoConMisiones() {
        Usuario logueado = new Usuario();
        logueado.setId(1L);

        when(sessionUtil.getUsuarioLogueado(request)).thenReturn(logueado);
        when(servicioMisiones.obtenerLasMisionesDelUsuario(logueado.getId())).thenReturn(misionesMock);

        return logueado;
    }

    private ModelAndView whenCargoLaVista(HttpServletRequest request) {
        return misionesController.misiones(request);
    }

    private void thenLaVistaMeDevueleveUnModelAnView(ModelAndView mav, Usuario logueado) {
        assertThat(mav.getViewName(), equalToIgnoringCase("misiones"));
        assertThat(mav.getModel().get("misiones"), equalTo(misionesMock));
    }

}
