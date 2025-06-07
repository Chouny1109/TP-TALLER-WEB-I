package com.tallerwebi.presentacion;

import com.tallerwebi.controller.PartidaController;
import com.tallerwebi.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PartidaControllerTest {


    @Test
    public void siElUsuarioEstaLogeado_seCargaLaPartida() {
        HttpServletRequest request = givenUsuarioEnSesion();
        ModelAndView mav = whenCargarPartida(request);
        thenSeCargaLaVistaConElJugador(mav);

    }

<<<<<<< HEAD

    /*
    @Test
    void testLlegaALaVistaYRetornaLaPreguntaCorrectamente(){
=======
    /*void testLlegaALaVistaYRetornaLaPreguntaCorrectamente(){
>>>>>>> c85b898 (WIP: cambios en PartidaController, Pregunta, Respuesta)
        // GIVEN
        // WHEN
        ModelAndView mav = whenQuieroIrALaVistaPreguntas();
        // THEN: se espera que la vista y el modelo sean correctos
        thenSeCargaLaVistaConLaPreguntaDefault(mav);
    }*/

    private HttpServletRequest givenUsuarioEnSesion() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        Usuario jugador = new Usuario("Nicolas127", "nico@caba.com", "123456");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("USUARIO")).thenReturn(jugador);

        return request;
    }

    private ModelAndView whenCargarPartida(HttpServletRequest request) {
        PartidaController partidaController = new PartidaController();
        return partidaController.cargarPartida(request);
    }

    private void thenSeCargaLaVistaConElJugador(ModelAndView mav) {
        assertEquals("cargarPartida", mav.getViewName());

        Usuario jugador = (Usuario) mav.getModel().get("jugador");

        assertEquals("Nicolas127", jugador.getNombreUsuario());
    }

    /*private ModelAndView whenQuieroIrALaVistaPreguntas(){
        PartidaController partidaController = new PartidaController();
        return partidaController.preguntas();
    }*/

    /*private void thenSeCargaLaVistaConLaPreguntaDefault(ModelAndView mav){
        assertEquals("preguntas", mav.getViewName());

        Pregunta preguntaEsperada  = new Pregunta("¿Color favorito?", Arrays.asList("Rojo", "Verde", "Azul"), TIPO_PREGUNTA.ENTRETENIMIENTO);

        Map<Integer, Pregunta> preguntas = (Map<Integer, Pregunta>) mav.getModel().get("preguntas");
        Pregunta preguntaObtenida = preguntas.get(1);

        assertEquals(preguntaEsperada, preguntaObtenida);
    }*/

}
