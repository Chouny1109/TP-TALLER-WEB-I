package com.tallerwebi.presentacion;

import com.tallerwebi.controller.PartidaController;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioPartida;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PartidaControllerTest {

    private ServicioPartida servicioPartida;
    private ServicioUsuario servicioUsuario;
    public PartidaControllerTest() {
        servicioPartida = mock(ServicioPartida.class);
        servicioUsuario = mock(ServicioUsuario.class);
    }

    @Test
    public void siElUsuarioEstaLogeado_seCargaLaPartida() {
        HttpServletRequest request = givenUsuarioEnSesion();

        ModelAndView mav = whenCargarPartida(request, TIPO_PARTIDA.MULTIJUGADOR);
        thenSeCargaLaVistaConElJugador(mav);

    }

    private HttpServletRequest givenUsuarioEnSesion() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        Usuario jugador = new Usuario("Nicolas127", "nico@caba.com", "123456");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("USUARIO")).thenReturn(jugador);

        return request;
    }

    private ModelAndView whenCargarPartida(HttpServletRequest request, TIPO_PARTIDA tipo) {
        PartidaController partidaController = new PartidaController(servicioPartida, servicioUsuario);
        return partidaController.cargarPartida(request, tipo);
    }

    private void thenSeCargaLaVistaConElJugador(ModelAndView mav) {
        assertEquals("cargarPartida", mav.getViewName());

        Usuario jugador = (Usuario) mav.getModel().get("jugador");

        assertEquals("Nicolas127", jugador.getNombreUsuario());
    }
    

}
