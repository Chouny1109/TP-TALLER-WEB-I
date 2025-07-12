package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.JugadorDTO;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.PartidaDTO;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioPartida;
import com.tallerwebi.service.ServicioRecovery;
import com.tallerwebi.service.impl.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final IServicioUsuario servicioUsuario;
    private final ServicioPartida servicioPartida;

    @Autowired
    public HomeController(IServicioUsuario servicioUsuario, ServicioPartida servicioPartida) {
        this.servicioUsuario = servicioUsuario;
        this.servicioPartida = servicioPartida;
    }

    @GetMapping
    public ModelAndView irAHome(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("home");

        Usuario usuario = (Usuario) request.getSession().getAttribute("USUARIO");

        if (usuario != null) {
            mav.addObject("nombreUsuario", usuario.getNombreUsuario());
            // Agregamos el atributo con el nombre esperado por el WebSocket
            request.getSession().setAttribute("usuario", usuario);
            mav.addObject("monedas", usuario.getMonedas());
            mav.addObject("vidas", usuario.getVidas());

        } else {
            return new ModelAndView("redirect:/login");
        }

        List<Partida> partidas = servicioPartida.obtenerPartidasAbiertasOEnCursoMultijugadorDeUnJugador(usuario);


        List<PartidaDTO> partidasDTO = partidas.stream()
                .map(p -> {
                    List<Usuario> jugadores = servicioPartida.obtenerJugadoresEnPartida(p.getId());

                    // Armamos los JugadorDTO y le seteamos el link del avatar
                    List<JugadorDTO> jugadorDTOs = jugadores.stream()
                            .map(jugador -> {
                                JugadorDTO dto = new JugadorDTO(jugador);
                                String avatar = servicioUsuario.obtenerImagenAvatarSeleccionado(jugador.getId());
                                dto.setLinkAvatar(avatar != null ? avatar : "/resources/imgs/default-avatar.png");
                                return dto;
                            })
                            .collect(Collectors.toList());

                    // Armamos el DTO de partida
                    PartidaDTO dto = new PartidaDTO(p);
                    dto.setJugadores(jugadorDTOs);
                    dto.setIdTurnoActual(p.getTurnoActual() != null ? p.getTurnoActual().getId() : null);

                    return dto;
                })
                .collect(Collectors.toList());


        mav.addObject("partidasMultijugador", partidasDTO);

        mav.addObject("usuarioId", usuario.getId());

        String avatarImg = this.servicioUsuario.obtenerImagenAvatarSeleccionado(usuario.getId());
        mav.addObject("avatarImg", avatarImg);
        mav.addObject("modos", TIPO_PARTIDA.values());

        return mav;
    }

}
