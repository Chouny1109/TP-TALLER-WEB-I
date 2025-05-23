package com.tallerwebi.service.impl;

import com.tallerwebi.model.*;
import com.tallerwebi.service.ServicioTienda;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioTiendaImpl implements ServicioTienda {
    @Override
    public List<Trampa> obtenerTrampas() {
        return List.of(
                new Trampa("Bomba", 100),
                new Trampa("Gas tóxico", 150),
                new Trampa("Trampa eléctrica", 180),
                new Trampa("Trampa láser", 200),
                new Trampa("Trampa de fuego", 250)
        );
    }

    @Override
    public List<Vida> obtenerVidas() {
        return List.of(
                new Vida("1 Vida", 200),
                new Vida("2 Vidas", 350),
                new Vida("3 Vidas", 500),
                new Vida("4 Vidas", 650),
                new Vida("5 Vidas", 800)
        );
    }

    @Override
    public List<Moneda> obtenerMonedas() {
        return List.of(
                new Moneda("x100 monedas", 200),
                new Moneda("x200 monedas", 400),
                new Moneda("x300 monedas", 600),
                new Moneda("x400 monedas", 800),
                new Moneda("x500 monedas", 1000)
        );
    }

    @Override
    public List<Avatar> obtenerAvatares() {
        Avatar a1 = new Avatar("Lia", 200);
        Avatar a2 = new Avatar("Pedro", 220);
        Avatar a3 = new Avatar("Cami", 250);
        Avatar a4 = new Avatar("Nacho", 300);
        Avatar a5 = new Avatar("Barto", 500);
        return List.of(a1, a2, a3, a4, a5);
    }
}
