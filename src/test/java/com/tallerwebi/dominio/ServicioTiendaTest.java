package com.tallerwebi.dominio;

import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioTienda;
import com.tallerwebi.service.ServicioTienda;
import com.tallerwebi.service.impl.ServicioTiendaImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        RepositorioTienda repositorioMock = mock(RepositorioTienda.class);

        when(repositorioMock.obtenerTrampas()).thenReturn(List.of(
                new Trampa("Bomba", 100),
                new Trampa("Gas tóxico", 150),
                new Trampa("Trampa eléctrica", 180),
                new Trampa("Trampa láser", 200),
                new Trampa("Trampa de fuego", 250)
        ));

        when(repositorioMock.obtenerVidas()).thenReturn(List.of(
                new Vida("1 Vida", 200),
                new Vida("2 Vidas", 350),
                new Vida("3 Vidas", 500),
                new Vida("4 Vidas", 650),
                new Vida("5 Vidas", 800)
        ));

        when(repositorioMock.obtenerMonedas()).thenReturn(List.of(
                new Moneda("x100", 200),
                new Moneda("x200", 400),
                new Moneda("x300", 600),
                new Moneda("x400", 800),
                new Moneda("x500", 1000)
        ));

        when(repositorioMock.obtenerAvatares()).thenReturn(List.of(
                new Avatar("Lia", 200),
                new Avatar("Pedro", 220),
                new Avatar("Cami", 250),
                new Avatar("Nacho", 300),
                new Avatar("Barto", 500)
        ));

        return new ServicioTiendaImpl(repositorioMock);
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
