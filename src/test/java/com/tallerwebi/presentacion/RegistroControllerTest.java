package com.tallerwebi.presentacion;

import com.tallerwebi.controller.RegistroController;
import com.tallerwebi.service.ServicioLogin;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class RegistroControllerTest {

    private RegistroController registroController;
    private Usuario usuarioMock;
    private ServicioLogin servicioLoginMock;
    private RedirectAttributes redirectAttributesMock;

    @BeforeEach
    public void init(){

        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
        when(usuarioMock.getPassword()).thenReturn("1234");
        servicioLoginMock = mock(ServicioLogin.class);
        registroController = new RegistroController(servicioLoginMock);
        redirectAttributesMock = mock(RedirectAttributes.class);
    }
    @Test
    public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente {

        // ejecucion
        ModelAndView modelAndView = registroController.registrarme(usuarioMock, "1234", redirectAttributesMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(servicioLoginMock, times(1)).registrar(usuarioMock);
        verify(redirectAttributesMock).addFlashAttribute(eq("exito"), anyString());

    }

    @Test
    public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
        // preparacion
        doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock);

        // ejecucion
        ModelAndView modelAndView = registroController.registrarme(usuarioMock, "1234", redirectAttributesMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
    }

    @Test
    public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
        // preparacion
        doThrow(RuntimeException.class).when(servicioLoginMock).registrar(usuarioMock);

        // ejecucion
        ModelAndView modelAndView = registroController.registrarme(usuarioMock, "1234", redirectAttributesMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
    }
}
