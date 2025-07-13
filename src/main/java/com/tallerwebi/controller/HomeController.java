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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
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
            servicioUsuario.regenerarVidasSiCorresponde(usuario);
            request.getSession().setAttribute("USUARIO", usuario);
            mav.addObject("nombreUsuario", usuario.getNombreUsuario());
            // Agregamos el atributo con el nombre esperado por el WebSocket
            request.getSession().setAttribute("usuario", usuario);
            mav.addObject("monedas", usuario.getMonedas());
            mav.addObject("vidas", usuario.getVidas());
            if (usuario.getVidas() < 5 && usuario.getUltimaRegeneracionVida() != null) {
                long segundosRestantes = Duration.between(LocalDateTime.now(),
                        usuario.getUltimaRegeneracionVida().plusHours(1)).getSeconds();
                if (segundosRestantes > 0) {
                    mav.addObject("tiempoRestanteVida", segundosRestantes);
                } else {
                    mav.addObject("tiempoRestanteVida", 0);
                }
            }



        } else {
            return new ModelAndView("redirect:/login");
        }

        HttpSession session = request.getSession();
        Integer xpGanado = (Integer) session.getAttribute("xpGanadoUltimoTurno");

        if (xpGanado != null) {
            mav.addObject("xpGanado", xpGanado);
            session.removeAttribute("xpGanadoUltimoTurno"); // lo quitás para que no quede fijo
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


         session = request.getSession();
        Boolean mostrarPopup = (Boolean) session.getAttribute("mostrarPopupVidas");
        String mensajeVidas = (String) session.getAttribute("mensajeVidas");

        if (Boolean.TRUE.equals(mostrarPopup) && mensajeVidas != null && !mensajeVidas.trim().isEmpty()) {
            mav.addObject("mensajeVidas", mensajeVidas);
        } else {
            // Por seguridad evitá agregar mensaje vacio
            mav.addObject("mensajeVidas", null);
        }

        session.removeAttribute("mensajeVidas");
        session.removeAttribute("mostrarPopupVidas");

        if (usuario.getVidas() < 5) {
            LocalDateTime ultima = usuario.getUltimaRegeneracionVida();
            Duration duracion = Duration.between(LocalDateTime.now(), ultima.plusHours(1));
            long segundosRestantes = duracion.getSeconds();
            mav.addObject("tiempoRestanteVida", segundosRestantes);
        }


        return mav;
    }

    @GetMapping("/verificar-regeneracion")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarRegeneracion(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO");

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        usuario = servicioUsuario.buscarUsuarioPorId(usuario.getId());
        int vidasAntes = usuario.getVidas();
        LocalDateTime ultimaRegen = usuario.getUltimaRegeneracionVida();
        long horasPasadas = Duration.between(ultimaRegen, LocalDateTime.now()).toHours();

        if (vidasAntes < 5 && horasPasadas >= 1) {
            int nuevasVidas = (int) Math.min(horasPasadas, 5 - vidasAntes);
            usuario.setVidas(vidasAntes + nuevasVidas);
            usuario.setUltimaRegeneracionVida(ultimaRegen.plusHours(nuevasVidas));
            servicioUsuario.actualizar(usuario);
        }

        session.setAttribute("USUARIO", usuario);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("vidas", usuario.getVidas());

        if (usuario.getVidas() < 5) {
            Duration nuevaEspera = Duration.between(LocalDateTime.now(), usuario.getUltimaRegeneracionVida().plusHours(1));
            respuesta.put("tiempoRestante", nuevaEspera.getSeconds());
        } else {
            respuesta.put("tiempoRestante", 0);
        }

        return ResponseEntity.ok(respuesta);
    }


}
