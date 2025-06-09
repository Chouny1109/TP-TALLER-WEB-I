package com.tallerwebi.service.impl;

import com.tallerwebi.model.*;
import com.tallerwebi.service.ServicioAmigos;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class ServicioAmigosImpl implements ServicioAmigos {

    @Override
    public List<Amigo> obtenerAmigos() {
        return Arrays.asList(
                new Amigo("Usuario_10", "/resources/imgs/perfilLia.jpg"),
                new Amigo("Usuario_23", "/resources/imgs/perfilAndrea.jpg"),
                new Amigo("Usuario_02", "/resources/imgs/perfilMadMax.jpg"),
                new Amigo("Usuario_8", "/resources/imgs/perfilMadMax.jpg"),
                new Amigo("Usuario_72", "/resources/imgs/perfilMadMax.jpg")

        );
    }
}
