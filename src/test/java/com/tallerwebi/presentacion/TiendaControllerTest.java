package com.tallerwebi.presentacion;

import com.tallerwebi.controller.TiendaController;
import com.tallerwebi.model.*;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioTienda;
import com.tallerwebi.service.ServicioTrampaUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TiendaControllerTest {

    @Test
    public void cuandoCargaTienda_devuelveLaVistaYModelos() {
        ServicioTienda servicioTienda = givenServicioTiendaConDatos();
        IServicioUsuario servicioUsuario = mock(IServicioUsuario.class);
        HttpSession session = mock(HttpSession.class);
        ServicioTrampaUsuario servicioTrampaUsuario = mock(ServicioTrampaUsuario.class);

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setMonedas(999);

        when(session.getAttribute("USUARIO")).thenReturn(usuarioMock);
        when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioMock);

        ModelAndView mav = whenCargarTienda(servicioTienda, servicioUsuario, session, servicioTrampaUsuario);

        thenVistaYModelosSonCorrectos(mav);
    }

    private ServicioTienda givenServicioTiendaConDatos() {
        ServicioTienda servicioTienda = mock(ServicioTienda.class);

        when(servicioTienda.obtenerTrampas()).thenReturn(List.of(new Trampa("Bomba", 100)));
        when(servicioTienda.obtenerVidas()).thenReturn(List.of(new Vida("1 Vida", 200)));
        when(servicioTienda.obtenerMonedas()).thenReturn(List.of(new Moneda("x100 monedas", 300)));
        when(servicioTienda.obtenerAvatares()).thenReturn(List.of(new Avatar("Lia", 200)));

        return servicioTienda;
    }

    private ModelAndView whenCargarTienda(ServicioTienda servicioTienda, IServicioUsuario servicioUsuario, HttpSession session, ServicioTrampaUsuario servicioTrampaUsuario) {
        TiendaController controller = new TiendaController(servicioTienda, servicioUsuario, servicioTrampaUsuario);
        return controller.cargarTienda(session);
    }

    private void thenVistaYModelosSonCorrectos(ModelAndView mav) {
        assertEquals("tienda", mav.getViewName());

        assertTrue(mav.getModel().containsKey("trampas"));
        assertTrue(mav.getModel().containsKey("vidas"));
        assertTrue(mav.getModel().containsKey("monedas"));
        assertTrue(mav.getModel().containsKey("avatares"));
        assertTrue(mav.getModel().containsKey("misMonedas"));

        assertEquals(1, ((List<?>) mav.getModel().get("trampas")).size());
        assertEquals(1, ((List<?>) mav.getModel().get("vidas")).size());
        assertEquals(1, ((List<?>) mav.getModel().get("monedas")).size());
        assertEquals(1, ((List<?>) mav.getModel().get("avatares")).size());
        assertEquals(999, mav.getModel().get("misMonedas"));
    }
}
