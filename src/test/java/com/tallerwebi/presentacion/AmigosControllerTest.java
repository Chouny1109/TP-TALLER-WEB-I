package com.tallerwebi.presentacion;

import com.tallerwebi.controller.AmigosController;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AmigosControllerTest {

    @Test
    public void cuandoSeIngresaAAmigosEntoncesSeMuestraLaVistaAmigos() {

        AmigosController amigosController = new AmigosController();

        ModelAndView mav = amigosController.verAmigos(null);

        entoncesLaVistaEsAmigos(mav);
    }

    private void entoncesLaVistaEsAmigos(ModelAndView mav) {
        assertEquals("amigos", mav.getViewName());
    }
}
