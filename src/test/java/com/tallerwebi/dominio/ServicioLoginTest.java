package com.tallerwebi.dominio;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.tallerwebi.model.DatosLogin;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;

import com.tallerwebi.service.impl.ServicioLoginImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ServicioLoginTest {

    private DatosLogin datosLogin;
    private RepositorioUsuario repositorioUsuario;
    private ServicioLoginImpl servicioLogin;
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    public void init() {
        datosLogin = mock(DatosLogin.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        passwordEncoder = new BCryptPasswordEncoder();
        servicioLogin = new ServicioLoginImpl(repositorioUsuario, passwordEncoder);

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
        usuarioMock.setEmail(email);
        usuarioMock.setPassword(passwordEncoder.encode(password)); // guarda la contraseña hasheada

        when(repositorioUsuario.buscar(email)).thenReturn(usuarioMock);
    }

    private void givenNoExisteUsuario() {
        when(repositorioUsuario.buscar(anyString())).thenReturn(null);
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