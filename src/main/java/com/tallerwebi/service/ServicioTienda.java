package com.tallerwebi.service;

import com.tallerwebi.model.*;

import java.util.List;

public interface ServicioTienda {
    List<Trampa> obtenerTrampas();
    List<Vida> obtenerVidas();
    List<Moneda> obtenerMonedas();
    List<Avatar> obtenerAvatares();
    Moneda obtenerMonedaPorId(Long id);
}
