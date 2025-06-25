package com.tallerwebi.controller;

import com.tallerwebi.model.SolicitudAmistad;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioSolicitudAmistad;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/amigos")
public class AmigosController {

    private final IServicioUsuario servicioUsuario;
    private final ServicioSolicitudAmistad servicioSolicitudAmistad;

    public AmigosController(IServicioUsuario servicioUsuario, ServicioSolicitudAmistad servicioSolicitudAmistad) {
        this.servicioUsuario = servicioUsuario;
        this.servicioSolicitudAmistad = servicioSolicitudAmistad;
    }

    @GetMapping
    public ModelAndView verAmigos(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO");
        Long idUsuario = usuario.getId();

        List<Usuario> amigos = servicioUsuario.obtenerAmigos(idUsuario);

        List<SolicitudAmistad> solicitudes = servicioSolicitudAmistad.obtenerSolicitudesRecibidas(usuario);

        ModelAndView mav = new ModelAndView("amigos");
        mav.addObject("amigos", amigos);
        mav.addObject("solicitudes", solicitudes);
        return mav;
    }

    @PostMapping("/aceptar")
    public String aceptarSolicitud(@RequestParam("idSolicitud") Long idSolicitud) {
        SolicitudAmistad solicitud = servicioSolicitudAmistad.buscarPorId(idSolicitud);
        if (solicitud != null) {
            servicioSolicitudAmistad.aceptarSolicitud(solicitud);
        }
        return "redirect:/amigos";
    }

    @PostMapping("/rechazar")
    public String rechazarSolicitud(@RequestParam("idSolicitud") Long idSolicitud) {
        SolicitudAmistad solicitud = servicioSolicitudAmistad.buscarPorId(idSolicitud);
        if (solicitud != null) {
            servicioSolicitudAmistad.rechazarSolicitud(solicitud);
        }
        return "redirect:/amigos";
    }

    @PostMapping("/enviar-solicitud")
    public String enviarSolicitud(@RequestParam("nombreUsuario") String nombreUsuario, HttpSession session) {
        Usuario remitente = (Usuario) session.getAttribute("USUARIO");
        Usuario destinatario = servicioUsuario.buscarPorNombreUsuario(nombreUsuario);

        if (destinatario != null && !remitente.equals(destinatario)) {
            boolean yaSonAmigos = servicioUsuario.obtenerAmigos(remitente.getId()).contains(destinatario);
            SolicitudAmistad existente = servicioSolicitudAmistad.buscarPorRemitenteYDestinatario(remitente, destinatario);

            if (!yaSonAmigos && existente == null) {
                servicioSolicitudAmistad.enviarSolicitud(remitente, destinatario);
            }
        }

        return "redirect:/amigos";
    }

    @PostMapping("/eliminar")
    public String eliminarAmigo(@RequestParam("idAmigo") Long idAmigo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO");
        servicioUsuario.eliminarAmigo(usuario.getId(), idAmigo);
        return "redirect:/amigos";
    }


}