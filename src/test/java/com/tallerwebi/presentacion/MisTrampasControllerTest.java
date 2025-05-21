package com.tallerwebi.presentacion;

import com.tallerwebi.controller.MisTrampasController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MisTrampasControllerTest {

    @Test
    public void cuandoSeIngresaAMisTrampasEntoncesSeMuestraLaVistaMisTrampas() {

        MisTrampasController controller = new MisTrampasController();

        String vista = controller.cargarTienda();

        entoncesLaVistaEsMisTrampas(vista);
    }

    private void entoncesLaVistaEsMisTrampas(String vista) {
        assertEquals("misTrampas", vista);
    }
}
