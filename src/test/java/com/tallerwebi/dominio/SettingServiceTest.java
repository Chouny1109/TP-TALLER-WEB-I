package com.tallerwebi.dominio;

import com.tallerwebi.components.InputField;
import com.tallerwebi.model.DatosSetting;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioSetting;
import com.tallerwebi.service.impl.ServicioSettingImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class SettingServiceTest {

    private RepositorioUsuario repositorioUsuario;
    private ServicioSetting servicioSetting;

    private DatosSetting datos;
    private Usuario usuario;


    @BeforeEach
    public void setUp() {
        repositorioUsuario = mock(RepositorioUsuario.class);
        servicioSetting = new ServicioSettingImpl(repositorioUsuario);
        datos = new DatosSetting();
        usuario = new Usuario();
    }

    @Test
    public void obtenerInputsFormDevuelveLosCamposCorrectos() {

        Usuario usuario = givenDadoUnUsuario();

        List<InputField> inputs = whenSeObtienenLosInputs(usuario);

        thenSeObtienenLosInputsCorrectamenteConElNameYelValueEsElAtributoDelUsuario(inputs);
    }

    private void thenSeObtienenLosInputsCorrectamenteConElNameYelValueEsElAtributoDelUsuario(List<InputField> inputs) {
        assertThat(inputs, hasSize(3));
        assertThat(inputs.get(0).getName(), is("userName"));
        assertThat(inputs.get(0).getValue(), is("eze"));
        assertThat(inputs.get(1).getName(), is("email"));
        assertThat(inputs.get(1).getValue(), is("eze@test.com"));
        assertThat(inputs.get(2).getName(), is("password"));
        assertThat(inputs.get(2).getValue(), is(nullValue()));
    }

    private List<InputField> whenSeObtienenLosInputs(Usuario usuario) {
        return servicioSetting.obtenerInputsForm(usuario);
    }

    private Usuario givenDadoUnUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("eze");
        usuario.setEmail("eze@test.com");
        usuario.setPassword("123");
        return usuario;
    }

    @Test
    public void actualizarUsuarioActualizaCorrectamenteYModificaElUsuario() {
        givenUnUsuarioYDatosSetting();
        whenSeActualizaElUsuario();
        thenElUsuarioSeActualizaCorrectamente();
    }

    private void thenElUsuarioSeActualizaCorrectamente() {
        assertThat(usuario.getEmail(), is("nuevo@test.com"));
        assertThat(usuario.getPassword(), is("newpass"));
        assertThat(usuario.getNombreUsuario(), is("nuevoNombre"));

        verify(repositorioUsuario).modificar(usuario);
    }

    private void whenSeActualizaElUsuario() {
        servicioSetting.actualizarUsuario("viejo@test.com", datos);
    }

    private void givenUnUsuarioYDatosSetting() {
        usuario.setEmail("viejo@test.com");
        usuario.setPassword("oldpass");
        usuario.setNombreUsuario("viejoNombre");

        datos.setEmail("nuevo@test.com");
        datos.setPassword("newpass");
        datos.setUserName("nuevoNombre");

        when(repositorioUsuario.buscar("viejo@test.com")).thenReturn(usuario);
    }


}
