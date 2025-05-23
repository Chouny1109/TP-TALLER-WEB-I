package com.tallerwebi.presentacion;

import com.tallerwebi.components.InputField;
import com.tallerwebi.controller.SettingsController;
import com.tallerwebi.model.DatosSetting;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioSetting;
import com.tallerwebi.util.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class SettingControllerTest {

    private ServicioSetting servicioSetting;
    private SessionUtil sessionUtil;
    private HttpServletRequest request;
    private SettingsController settingsController;
    private DatosSetting datosSetting;
    private InputField inputFields;

    private final Usuario usuarioMock = new Usuario();
    private final List<InputField> inputsFieldMock = List.of(new InputField("email", "email", "Email", usuarioMock.getEmail()));

    @BeforeEach
    public void setUp() {
        servicioSetting = mock(ServicioSetting.class);
        sessionUtil = mock(SessionUtil.class);
        request = mock(HttpServletRequest.class);
        settingsController = new SettingsController(servicioSetting, sessionUtil);
        usuarioMock.setId(1L);
        usuarioMock.setEmail("lucieze02@icloud.com");
        datosSetting = new DatosSetting();
        inputFields = mock(InputField.class);
    }

    @Test
    public void cuandoCargoLaVistaSettingMeDevuelveUnModelAndViewConUnArrayDeInputsYUnUsuarioLogueado() {
        givenUsuarioLogueadoEInputs();

        ModelAndView mav = whenSeCargaLaVistaSetting();

        thenSeDevuelveLaVistaCorrespondiente(mav);
    }

    private void thenSeDevuelveLaVistaCorrespondiente(ModelAndView mav) {
        assertThat(mav.getViewName(), equalToIgnoringCase("settings"));
        assertThat(mav.getModel().get("inputFields"), equalTo(inputsFieldMock));
        assertThat(mav.getModel().get("usuarioLogueado"), equalTo(usuarioMock));
    }

    private ModelAndView whenSeCargaLaVistaSetting() {
        return settingsController.cargarSettings(request);
    }

    private void givenUsuarioLogueadoEInputs() {
        when(sessionUtil.getUsuarioLogueado(request)).thenReturn(usuarioMock);
        when(servicioSetting.obtenerInputsForm(usuarioMock)).thenReturn(inputsFieldMock);
    }


    @Test
    public void cuandoSeActualizaElUsuarioRedirigeAYActualizaLaSesion() {

        Usuario actualizado = givenUsuarioLogueadoYUsuarioActualizado(datosSetting);

        String redireccion = whenSeActualizaElUsuario(datosSetting);

        thenSeRedirigeYSeActualizaLaSesion(redireccion, datosSetting);
    }

    private Usuario givenUsuarioLogueadoYUsuarioActualizado(DatosSetting datosSetting) {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setEmail("nuevo@test.com");

        when(sessionUtil.getUsuarioLogueado(request)).thenReturn(usuarioMock);
        when(servicioSetting.obtenerUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioActualizado);

        return usuarioActualizado;
    }

    private String whenSeActualizaElUsuario(DatosSetting datosSetting) {
        return settingsController.actualizarUsuario(datosSetting, request);
    }

    private void thenSeRedirigeYSeActualizaLaSesion(String redireccion, DatosSetting datosSetting) {
        verify(servicioSetting).actualizarUsuario(usuarioMock.getEmail(), datosSetting);
        verify(sessionUtil).setUsuarioEnSession(eq(request), any(Usuario.class));
        assertThat(redireccion, equalTo("redirect:/settings"));
    }
}
