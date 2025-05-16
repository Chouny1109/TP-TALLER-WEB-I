package com.tallerwebi.presentacion;



import com.tallerwebi.controller.RegistroController;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.model.DatosRegistro;
import com.tallerwebi.service.ServicioRegistro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class RegistroControllerTest {

    private RegistroController registroController;

    private DatosRegistro datosRegistroMock;

    private ServicioRegistro servicioRegistroMock;

    private RedirectAttributes redirectAttributesMock;


    @BeforeEach
    public void init(){

        datosRegistroMock = mock(DatosRegistro.class);

        when(datosRegistroMock.getPassword()).thenReturn("1234");
        when(datosRegistroMock.getNombreUsuario()).thenReturn("dami");

        servicioRegistroMock = mock(ServicioRegistro.class);
        registroController = new RegistroController(servicioRegistroMock);
        redirectAttributesMock = mock(RedirectAttributes.class);


    }

    //* ------------------------------------ TEST ------------------------------------*//

    //* ------------------------------------ TEST REGISTRO EXITOSO ------------------------------------*//
    @Test
    public void siNoExisteUsuarioSeDebeCrearUsuarioYVolverAlLogin() throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        givenNoExisteUsuario();
        when(datosRegistroMock.getEmail()).thenReturn("damiunlam@gmail.com");
        when(datosRegistroMock.getConfirmarPassword()).thenReturn("1234");

        ModelAndView mav = whenRegistroUsuario();
        thenRegistroExtitosoYVuelveAlLogin(mav);
    }

    //* ------------------------------------ TEST REGISTRO NO EXITOSO ------------------------------------*//
    @Test
    public void siExisteUsuarioNoSeDebeCrearUsuarioYVuelveAlRegistroYMostrarError() throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        givenExisteUsuario();
        when(datosRegistroMock.getEmail()).thenReturn("damiunlam@gmail.com");
        when(datosRegistroMock.getConfirmarPassword()).thenReturn("1234");
        ModelAndView mav = whenRegistroUsuario();
        thenRegistroNoExtitosoVuelveAlRegistroYMuestraError(mav, "El usuario ya existe");

    }

    @Test
    public void siLasPasswordsNoCoincidenVuelveAlRegistroYMostrarError() throws PasswordsNotEquals, EmailInvalido, UsuarioExistente {
        givenLasPasswordsNoCoinciden();
        when(datosRegistroMock.getEmail()).thenReturn("damiunlam@gmail.com");
        when(datosRegistroMock.getConfirmarPassword()).thenReturn("12345");
        ModelAndView mav = whenRegistroUsuario();
        thenRegistroNoExtitosoVuelveAlRegistroYMuestraError(mav, "Las contraseñas no coinciden");
    }



    @Test
    public void siElEmailNoContieneArrobaVuelveAlRegistroYMostrarError() throws EmailInvalido, PasswordsNotEquals, UsuarioExistente {
        when(datosRegistroMock.getEmail()).thenReturn("damiunlam.com"); // sin '.com'

        // Seteo también el password y confirm password para que el flujo no falle antes
        when(datosRegistroMock.getConfirmarPassword()).thenReturn("1234");


        givenElEmailEsInvalido();

        ModelAndView mav = whenRegistroUsuario();

        thenRegistroNoExtitosoVuelveAlRegistroYMuestraError(mav, "El email debe contener '@' y '.com'");

    }

    @Test
    public void siElEmailNoContienePuntoComVuelveAlRegistroYMostrarError() throws EmailInvalido, PasswordsNotEquals, UsuarioExistente {
        // Doy un email inválido que no contiene ".com"
        when(datosRegistroMock.getEmail()).thenReturn("damiunlam@"); // sin '.com'

        // Seteo también el password y confirm password para que el flujo no falle antes
        when(datosRegistroMock.getConfirmarPassword()).thenReturn("12345");


        givenElEmailEsInvalido();

        ModelAndView mav = whenRegistroUsuario();

        thenRegistroNoExtitosoVuelveAlRegistroYMuestraError(mav, "El email debe contener '@' y '.com'");
    }

    //* ------------------------------------ GIVEN ------------------------------------*//

    private void givenElEmailEsInvalido() throws PasswordsNotEquals, EmailInvalido, UsuarioExistente {
        doThrow(new EmailInvalido())
                .when(servicioRegistroMock)
                .registrar(argThat(usuario -> {
                    String email = usuario.getEmail();
                    // Condición que detecta email inválido (sin @ o sin .com)
                    return !email.contains("@") || !email.contains(".com");
                }), anyString());

    }

    private void givenLasPasswordsNoCoinciden() throws PasswordsNotEquals, EmailInvalido, UsuarioExistente {
        doThrow(new PasswordsNotEquals())
                .when(servicioRegistroMock)
                .registrar(any(Usuario.class), argThat(confirmar -> !confirmar.equals(datosRegistroMock.getPassword())));
    }


    private void givenExisteUsuario() throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        when(datosRegistroMock.getEmail()).thenReturn("dami@unlam.com");
        doThrow(new UsuarioExistente())
        .when(servicioRegistroMock).registrar(argThat((Usuario u) -> u.getEmail().equalsIgnoreCase(datosRegistroMock.getEmail())), anyString());
    }

    private void givenNoExisteUsuario() {
    }


    //* ------------------------------------ WHEN ------------------------------------*//
    private ModelAndView whenRegistroUsuario() {

            return registroController.registrarme(datosRegistroMock, redirectAttributesMock);
    }


    //* ------------------------------------ THEN ------------------------------------*//

    private void thenRegistroExtitosoYVuelveAlLogin(ModelAndView mav) throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        Usuario usuario = new Usuario();
        usuario.setEmail(datosRegistroMock.getEmail());
        usuario.setPassword(datosRegistroMock.getPassword());
        usuario.setNombreUsuario(datosRegistroMock.getNombreUsuario());
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(servicioRegistroMock).registrar(argThat(u ->
                u.getEmail().equalsIgnoreCase(datosRegistroMock.getEmail()) &&
                        u.getNombreUsuario().equals(datosRegistroMock.getNombreUsuario())
        ), anyString());
        verify(redirectAttributesMock).addFlashAttribute(eq("exito"), anyString());
    }


    private void thenRegistroNoExtitosoVuelveAlRegistroYMuestraError(ModelAndView mav, String mensajeEsperado) {
        assertThat(mav.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(mav.getModel().get("error").toString(), equalToIgnoringCase(mensajeEsperado));
    }



}
