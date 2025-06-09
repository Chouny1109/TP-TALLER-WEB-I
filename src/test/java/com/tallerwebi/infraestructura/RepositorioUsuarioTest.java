//package com.tallerwebi.infraestructura;
//
//import com.tallerwebi.config.HibernateConfig;
//import com.tallerwebi.config.SpringWebConfig;
//import com.tallerwebi.model.Mision;
//import com.tallerwebi.repository.RepositorioMisiones;
//import com.tallerwebi.repository.RepositorioUsuario;
//import com.tallerwebi.repository.impl.RepositorioMisionesImpl;
//import com.tallerwebi.repository.impl.RepositorioUsuarioImpl;
//import com.tallerwebi.service.impl.ServicioMisionesImpl;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.List;
//
//import static org.mockito.Mockito.mock;
//
//@ExtendWith({SpringExtension.class})
//@WebAppConfiguration
//@ContextConfiguration(classes = {SpringWebConfig.class, HibernateConfig.class})
//public class RepositorioUsuarioTest {
//
//    private SessionFactory sessionFactory;
//    private RepositorioUsuario repositorioUsuario;
//    private RepositorioMisiones repositorioMisiones;
//
//
//    @BeforeEach
//    public void setUp() {
//        repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
//        repositorioMisiones = new RepositorioMisionesImpl(sessionFactory);
//    }
//
//    @Test
//    public void dadoUnUsuarioLogueadoPuedoObtenerSuListaDeMisionesPorId() {
//
//        givenUnaListaDeMisionesYUnUsuario();
//
//        List<Mision> misiones = whenBuscoLasMisionesDelUsuarioPorId(1L);
//
//        thenEncuentroLasMisiones(misiones);
//
//    }
//
//    private void givenUnaListaDeMisionesYUnUsuario() {
//
//    }
//
//    private List<Mision> whenBuscoLasMisionesDelUsuarioPorId(Long idUsuario) {
//        return this.repositorioMisiones.misionesDeUsuario(idUsuario);
//    }
//
//    private void thenEncuentroLasMisiones(List<Mision>misiones) {
//
//    }
//
//
//}
