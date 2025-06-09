package com.tallerwebi.components;

import com.tallerwebi.service.ServicioMisiones;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AsignacionDeTareasDiarias {
    private final ServicioMisiones servicioMisiones;

    public AsignacionDeTareasDiarias(ServicioMisiones servicioMisiones) {
        this.servicioMisiones = servicioMisiones;
    }

    @Scheduled(fixedRate = 1000)
    public void asignarTareasDiarias() {
        this.servicioMisiones.asignarMisionesDiarias();
    }
}
