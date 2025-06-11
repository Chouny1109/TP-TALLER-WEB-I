package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.model.ResultadoRegistro;
import com.tallerwebi.model.Usuario;

import com.tallerwebi.model.DatosRegistro;

import com.tallerwebi.repository.RepositorioAvatar;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioRegistro;
import com.tallerwebi.service.impl.ServicioRegistroImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;



public class ServicioRegistroTest {


    private RepositorioUsuario repositorioUsuario;
    private ServicioRegistro servicioRegistro;
    private DatosRegistro datosRegistroMock;
    private Usuario usuariomock ;
    private RepositorioAvatar repositorioAvatar;
    @BeforeEach
    public void init() {
        repositorioUsuario = mock(RepositorioUsuario.class);
        repositorioAvatar = mock(RepositorioAvatar.class);
        servicioRegistro = new ServicioRegistroImpl(repositorioUsuario, repositorioAvatar);
        datosRegistroMock = mock(DatosRegistro.class);

        String password = "1234";
        String nombreUsuario = "dami";

        when(datosRegistroMock.getPassword()).thenReturn(password);
        when(datosRegistroMock.getNombreUsuario()).thenReturn(nombreUsuario);

        usuariomock = new Usuario();
        usuariomock.setPassword(password);
        usuariomock.setNombreUsuario(nombreUsuario);

    }

    //* ------------------------------------ TEST ------------------------------------*//

    //* ------------------------------------ TEST REGISTRO EXITOSO ------------------------------------*//

    @Test
    public void siNoExisteUsuarioSeDebeCrearUsuarioYVolverAlLogin() throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        givenNoExisteUsuario();
        String email = "dami@gmail.com";
        String password = "1234";
        when(datosRegistroMock.getEmail()).thenReturn(email);
        usuariomock.setEmail(email);
        when(datosRegistroMock.getConfirmarPassword()).thenReturn(password);


        ResultadoRegistro registrado = whenRegistroUsuario();
        thenRegistroExtitoso(registrado);
    }

    @Test
    public void siPasswordCoincideConConfirmadoPasswordDevuelveElLogin() throws PasswordsNotEquals, EmailInvalido, UsuarioExistente {
        givenNoExisteUsuario();
        String email = "dami@gmail.com";
        String password = "1234";
        when(datosRegistroMock.getEmail()).thenReturn(email);
        usuariomock.setEmail(email);
        when(datosRegistroMock.getConfirmarPassword()).thenReturn(password);


        ResultadoRegistro registrado = whenRegistroUsuario();
        thenRegistroExtitoso(registrado);
    }

    @Test
    public void contraseñaDebeGuardarseEncriptada() throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        givenNoExisteUsuario();
        String email = "dami@gmail.com";
        String password = "1234";
        when(datosRegistroMock.getEmail()).thenReturn(email);
        usuariomock.setEmail(email);
        when(datosRegistroMock.getConfirmarPassword()).thenReturn(password);

        when(repositorioUsuario.guardar(any())).thenReturn(true);
        whenRegistroUsuario();

        thenPasswordDebeEstarEncriptado(password);
    }



    //* ------------------------------------ TEST REGISTRO NO EXITOSO ------------------------------------*//

    @Test
    public void siExisteUsuarioNoSeDebeCrearUsuarioYVuelveAlRegistroYMostrarError() throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        String email = "dami@gmail.com";
        String password = "1234";
        when(datosRegistroMock.getEmail()).thenReturn(email);
        usuariomock.setEmail(email);
        when(datosRegistroMock.getConfirmarPassword()).thenReturn(password);

        givenExisteUsuario();
        ResultadoRegistro registrado = whenRegistroUsuario();
        thenRegistroNoExtitosoRegistroNoExitosoYLanzaExcepcion(registrado, UsuarioExistente.class);

    }
    @Test
    public void siPasswordNoCoincideConConfirmarPassword() throws PasswordsNotEquals, EmailInvalido, UsuarioExistente {
        givenNoExisteUsuario();
        String email = "dami@gmail.com";
        String password = "12345";
        when(datosRegistroMock.getEmail()).thenReturn(email);
        usuariomock.setEmail(email);
        when(datosRegistroMock.getConfirmarPassword()).thenReturn(password);


        ResultadoRegistro registrado = whenRegistroUsuario();
        thenRegistroNoExtitosoRegistroNoExitosoYLanzaExcepcion(registrado, PasswordsNotEquals.class);
    }


    @Test
    public void siElEmailNoTieneArrobaVuelveAlRegistroYMostrarError() throws EmailInvalido, PasswordsNotEquals, UsuarioExistente {
        givenNoExisteUsuario();
        String email = "damigamil.com";
        String password = "1234";
        when(datosRegistroMock.getEmail()).thenReturn(email);
        usuariomock.setEmail(email);
        when(datosRegistroMock.getConfirmarPassword()).thenReturn(password);

        ResultadoRegistro registrado = whenRegistroUsuario();
        thenRegistroNoExtitosoRegistroNoExitosoYLanzaExcepcion(registrado, EmailInvalido.class);
    }

    @Test
    public void siElEmailNoTienePuntoComVuelveAlRegistroYMostrarError() throws EmailInvalido, PasswordsNotEquals, UsuarioExistente {
        givenNoExisteUsuario();
        String email = "dami@gamil";
        String password = "1234";
        when(datosRegistroMock.getEmail()).thenReturn(email);
        usuariomock.setEmail(email);
        when(datosRegistroMock.getConfirmarPassword()).thenReturn(password);

        ResultadoRegistro registrado = whenRegistroUsuario();

        thenRegistroNoExtitosoRegistroNoExitosoYLanzaExcepcion(registrado,  EmailInvalido.class);
    }


    //* ------------------------------------ GIVEN ------------------------------------*//
    private void givenNoExisteUsuario() {

        when(repositorioUsuario.buscar(anyString())).thenReturn(null);


    }

    private void givenExisteUsuario() throws PasswordsNotEquals, EmailInvalido, UsuarioExistente {

        when(repositorioUsuario.buscar(anyString())).thenReturn(new Usuario());


    }

    //* ------------------------------------ WHEN ------------------------------------*//

    private ResultadoRegistro whenRegistroUsuario() throws UsuarioExistente, PasswordsNotEquals, EmailInvalido {
        ResultadoRegistro resultado = new ResultadoRegistro();
        try {
            servicioRegistro.registrar(usuariomock, datosRegistroMock.getConfirmarPassword());
            resultado.setExitoso(Boolean.TRUE);
        } catch (Exception e) {
            resultado.error = e;
        }
        return resultado;

    }

    //* ------------------------------------ THEN ------------------------------------*//
    private void thenRegistroNoExtitosoRegistroNoExitosoYLanzaExcepcion(ResultadoRegistro registrado, Class<? extends Exception> exception) {
        assertThat(registrado.huboError(),is(Boolean.TRUE));
        assertThat(registrado.getError(),instanceOf(exception));
    }

    private void thenRegistroExtitoso(ResultadoRegistro registrado)  {
        assertThat(registrado.fueExitoso(),is(Boolean.TRUE));
    }

    private void thenPasswordDebeEstarEncriptado(String password) {
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repositorioUsuario).guardar(captor.capture());
        Usuario usuarioGuardado = captor.getValue();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(password, usuarioGuardado.getPassword()));
    }



}
