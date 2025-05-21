package com.tallerwebi.dominio;

import com.tallerwebi.model.*;
import com.tallerwebi.service.ServicioTienda;
import com.tallerwebi.service.impl.ServicioTiendaImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServicioTiendaTest {
    @Test
    public void devuelveCincoTrampas() {
        ServicioTiendaImpl servicio = givenServicio();

        List<Trampa> trampas = whenObtenerTrampas(servicio);
        
        thenHayCincoItems(trampas);
    }

    @Test
    public void devuelveCincoVidas() {
        ServicioTiendaImpl servicio = givenServicio();
        List<Vida> vidas = whenObtenerVidas(servicio);
        thenHayCincoItems(vidas);
    }

    @Test
    public void devuelveCincoMonedas() {
        ServicioTiendaImpl servicio = givenServicio();
        List<Moneda> monedas = whenObtenerMonedas(servicio);
        thenHayCincoItems(monedas);
    }

    @Test
    public void devuelveCincoAvatares() {
        ServicioTiendaImpl servicio = givenServicio();
        List<Avatar> avatares = whenObtenerAvatares(servicio);
        thenHayCincoItems(avatares);
    }

    private ServicioTiendaImpl givenServicio() {
        return new ServicioTiendaImpl();
    }

    private List<Trampa> whenObtenerTrampas(ServicioTiendaImpl servicio) {
        return servicio.obtenerTrampas();
    }

    private static List<Vida> whenObtenerVidas(ServicioTiendaImpl servicio) {
        return servicio.obtenerVidas();
    }

    private static List<Moneda> whenObtenerMonedas(ServicioTiendaImpl servicio) {
        return servicio.obtenerMonedas();
    }

    private static List<Avatar> whenObtenerAvatares(ServicioTiendaImpl servicio) {
        return servicio.obtenerAvatares();
    }

    private void thenHayCincoItems(List<?> lista) {
        assertEquals(5, lista.size());
    }
}
