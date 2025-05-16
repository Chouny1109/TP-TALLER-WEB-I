package com.tallerwebi.presentacion;

import com.tallerwebi.controller.ControladorLogin;
import com.tallerwebi.controller.RegistroController;
import com.tallerwebi.dominio.enums.ROL_USUARIO;
import com.tallerwebi.service.ServicioLogin;
import com.tallerwebi.model.DatosLogin;
import com.tallerwebi.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private RegistroController registroController;
	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;



	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "1234");
		usuarioMock = mock(Usuario.class);
		when(usuarioMock.getNombreUsuario()).thenReturn("dami123");
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");


		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		when(requestMock.getSession()).thenReturn(sessionMock);
		servicioLoginMock = mock(ServicioLogin.class);
		registroController = new RegistroController(servicioLoginMock);
		controladorLogin = new ControladorLogin(servicioLoginMock);
	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
		// preparacion
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
	}
	
	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome(){
		// preparacion
		Usuario usuarioEncontradoMock = mock(Usuario.class);
		when(usuarioEncontradoMock.getRol()).thenReturn(ROL_USUARIO.ADMIN);
		when(usuarioEncontradoMock.getNombreUsuario()).thenReturn("dami123");
		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);

		when(sessionMock.getAttribute("USUARIO")).thenReturn(usuarioEncontradoMock);
		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);
		
		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
		verify(sessionMock, times(1)).setAttribute("ROL", usuarioEncontradoMock.getRol());
		verify(sessionMock, times(1)).setAttribute("USUARIO", usuarioEncontradoMock);
	}

}
