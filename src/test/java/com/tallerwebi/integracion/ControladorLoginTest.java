package com.tallerwebi.integracion;

import com.tallerwebi.config.WebSocketConfig;
import com.tallerwebi.integracion.config.*;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.ServicioRecovery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
		SpringWebTestConfig.class,
		HibernateTestConfig.class,
		MailTestConfig.class,
		MockServicioRecoveryConfig.class,
		MockSimpMessagingTemplateConfig.class,
		MockSimpUserRegistryConfig.class,
		com.tallerwebi.config.DotenvConfig.class
})
public class ControladorLoginTest {

	private Usuario usuarioMock;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ServicioRecovery servicioRecovery;
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@BeforeEach
	public void init(){
		usuarioMock = mock(Usuario.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void debeRetornarLaPaginaLoginCuandoSeNavegaALaRaiz() throws Exception {

		MvcResult result = this.mockMvc.perform(get("/"))
				/*.andDo(print())*/
				.andExpect(status().is3xxRedirection())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
		assertThat("redirect:/login", equalToIgnoringCase(Objects.requireNonNull(modelAndView.getViewName())));
		assertThat(true, is(modelAndView.getModel().isEmpty()));
	}

	@Test
	public void debeRetornarLaPaginaLoginCuandoSeNavegaALLogin() throws Exception {

		MvcResult result = this.mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("datosLogin").toString(),  containsString("com.tallerwebi.model.DatosLogin"));

	}
}
