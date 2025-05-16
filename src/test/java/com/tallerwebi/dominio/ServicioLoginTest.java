package com.tallerwebi.dominio;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.tallerwebi.presentacion.DatosLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioLoginTest {

    private DatosLogin datosLogin;
    private RepositorioUsuario repositorioUsuario;
    private ServicioLoginImpl servicioLogin;

    @BeforeEach
    public void init() {
        datosLogin = mock(DatosLogin.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        servicioLogin = new ServicioLoginImpl(repositorioUsuario);

        // valores por defecto para datosLogin (pueden ser sobreescritos en tests)
        when(datosLogin.getEmail()).thenReturn("damiunlam@gmail.com");
        when(datosLogin.getPassword()).thenReturn("1234");
    }

    //* ------------------------------------ TEST ------------------------------------*//

    //* ------------------------------------ TEST LOGUEO EXITOSO ------------------------------------*//

    @Test
    public void siHayEmailYPasswordExistenteLoginExitoso() {
        // dado que existe usuario para estos datos
        givenExisteUsuario("damiunlam@gmail.com", "1234");

        // cuando se loguea el usuario
        Usuario usuario = whenUsuarioSeLoguea();

        // entonces el logueo es exitoso (usuario != null)
        thenLogueoExitoso(usuario);
    }

    //* ------------------------------------ TEST LOGUEO EXITOSO ------------------------------------*//

    @Test
    public void siNoHayEmailYUsuarioExistenteLoginNoExitoso() {
        // dado que NO existe usuario para cualquier email y password
        givenNoExisteUsuario();

        // cuando se loguea el usuario
        Usuario usuario = whenUsuarioSeLoguea();

        // entonces el logueo NO es exitoso (usuario == null)
        thenLogueoNoExitoso(usuario);
    }

    //* ------------------------------------ GIVEN ------------------------------------*//

    private void givenExisteUsuario(String email, String password) {
        Usuario usuarioMock = new Usuario();
        when(repositorioUsuario.buscarUsuario(email, password)).thenReturn(usuarioMock);
    }

    private void givenNoExisteUsuario() {
        when(repositorioUsuario.buscarUsuario(anyString(), anyString())).thenReturn(null);
    }

    //* ------------------------------------ WHEN ------------------------------------*//

    private Usuario whenUsuarioSeLoguea() {
        return servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
    }

    //* ------------------------------------ THEN ------------------------------------*//

    private void thenLogueoExitoso(Usuario usuario) {
        assertThat(usuario, is(notNullValue()));
    }

    private void thenLogueoNoExitoso(Usuario usuario) {
        assertThat(usuario, is(nullValue()));
    }
}
