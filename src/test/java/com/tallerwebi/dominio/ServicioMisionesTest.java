//package com.tallerwebi.dominio;
//
//import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
//import com.tallerwebi.model.Mision;
//import com.tallerwebi.model.Usuario;
//import com.tallerwebi.repository.RepositorioMisiones;
//import com.tallerwebi.repository.RepositorioUsuario;
//import com.tallerwebi.service.ServicioMisiones;
//import com.tallerwebi.service.impl.ServicioMisionesImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class ServicioMisionesTest {
//
//    private RepositorioUsuario repositorioUsuario;
//    private ServicioMisiones servicioMisiones;
//    private final Long usuarioId = 1L;
//    private final List<Mision> misionesMock = List.of(
//            new Mision("Titulo 1", "Descripcion 1"),
//            new Mision("Titulo 2", "Descripcion 2")
//    );
//    private Usuario usuarioMock;
//    private RepositorioMisiones repositorioMisiones;
//
//    @BeforeEach
//    public void setUp() {
//        repositorioUsuario = mock(RepositorioUsuario.class);
//        servicioMisiones = new ServicioMisionesImpl(repositorioMisiones,repositorioUsuario);
//    }
//
//    @Test
//    public void cuandoObtengoLasMisionesPorUsuarioDevuelveLaListEsperada() throws UsuarioNoExistente {
//        givenDadoUnUsuarioConMisiones(usuarioId);
//        List<Mision> misiones = whenObtengoLasMisionesDelUsuario();
//        thenSeDevuelvenLasMisionesDelUsuario(misiones);
//    }
//
//    private void thenSeDevuelvenLasMisionesDelUsuario(List<Mision> misiones) {
//        assertThat(misiones, equalTo(misionesMock));
//    }
//
//    private List<Mision> whenObtengoLasMisionesDelUsuario() throws UsuarioNoExistente {
//        return servicioMisiones.obtenerLasMisionesDelUsuario(usuarioId);
//    }
//
//    private void givenDadoUnUsuarioConMisiones(Long usuarioId) {
//        usuarioMock = new Usuario();
//        usuarioMock.setId(usuarioId);
//        usuarioMock.setMisiones(misionesMock);
//
//        when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuarioMock);
//    }
//}
