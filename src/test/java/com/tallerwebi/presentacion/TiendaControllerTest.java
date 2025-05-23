package com.tallerwebi.presentacion;

import com.tallerwebi.controller.TiendaController;
import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Moneda;
import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Vida;
import com.tallerwebi.service.ServicioTienda;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TiendaControllerTest {

    @Test
    public void cuandoCargaTienda_devuelveLaVistaYModelos() {
        ServicioTienda servicioTienda = givenServicioTiendaConDatos();

        ModelAndView mav = whenCargarTienda(servicioTienda);

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

    private ModelAndView whenCargarTienda(ServicioTienda servicioTienda) {
        TiendaController controller = new TiendaController(servicioTienda);
        return controller.cargarTienda();
    }

    private void thenVistaYModelosSonCorrectos(ModelAndView mav) {
        assertEquals("tienda", mav.getViewName());

        assertTrue(mav.getModel().containsKey("trampas"));
        assertTrue(mav.getModel().containsKey("vidas"));
        assertTrue(mav.getModel().containsKey("monedas"));
        assertTrue(mav.getModel().containsKey("avatares"));

        assertEquals(1, ((List<?>) mav.getModel().get("trampas")).size());
        assertEquals(1, ((List<?>) mav.getModel().get("vidas")).size());
        assertEquals(1, ((List<?>) mav.getModel().get("monedas")).size());
        assertEquals(1, ((List<?>) mav.getModel().get("avatares")).size());
    }
}
