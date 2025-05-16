package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ROL_USUARIO;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.dom4j.rule.Mode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {


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
		when(usuarioMock.getRol()).thenReturn(ROL_USUARIO.ADMIN);
		when(usuarioMock.getNombreUsuario()).thenReturn("dami123");
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		when(requestMock.getSession()).thenReturn(sessionMock);
		servicioLoginMock = mock(ServicioLogin.class);
		controladorLogin = new ControladorLogin(servicioLoginMock);
	}


	//* ------------------------------------ TEST ------------------------------------*//

	@Test
	public void siElEmailYPasswordSonCorrectosEntoncesDebeLlevarAlHome(){
		givenExisteUnUsuario();
		ModelAndView mav = whenUsuarioSeLoguea();
		thenSeRedirigeAlHome(mav);
	}
	@Test
	public void siElMailYPasswordSonIncorrectosEntoncesDebeLlevarDeNuevoAlLogin(){
		givenNoExisteUsuario();
		ModelAndView mav = whenUsuarioSeLoguea();
		thenSeVuelveAlLogin(mav);
	}

	//* ------------------------------------ GIVEN ------------------------------------*//

	private void givenExisteUnUsuario() {
		when(sessionMock.getAttribute("USUARIO")).thenReturn(usuarioMock);
		when(servicioLoginMock.consultarUsuario("dami@unlam.com", "1234")).thenReturn(usuarioMock);
	}
	private void givenNoExisteUsuario() {
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);
	}

	//* ------------------------------------ WHEN ------------------------------------*//

	private ModelAndView whenUsuarioSeLoguea() {
		try {
			return controladorLogin.validarLogin(datosLoginMock, requestMock);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	//* ------------------------------------ THEN ------------------------------------*//

	private void thenSeVuelveAlLogin(ModelAndView mav) {
		assertThat(mav.getViewName(), equalToIgnoringCase("login"));
		assertThat(mav.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		verify(sessionMock, never()).setAttribute(eq("ROL"), any());
		verify(sessionMock, never()).setAttribute(eq("USUARIO"), any());
	}
	private void thenSeRedirigeAlHome(ModelAndView mav) {
		assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/home"));
		verify(sessionMock, times(1)).setAttribute("ROL", usuarioMock.getRol());
		verify(sessionMock, times(1)).setAttribute("USUARIO", usuarioMock);
	}


}
