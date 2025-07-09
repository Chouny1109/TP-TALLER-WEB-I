package com.tallerwebi.controller;

import com.tallerwebi.model.Mensaje;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.IServicioUsuario;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final IServicioUsuario servicioUsuario;

    public ChatController(SimpMessagingTemplate messagingTemplate, IServicioUsuario servicioUsuario) {
        this.messagingTemplate = messagingTemplate;
        this.servicioUsuario = servicioUsuario;
    }

    @MessageMapping("/chat/privado")
    public void enviarMensajePrivado(@Payload Mensaje mensaje) {
        System.out.println("   Intentando enviar mensaje:");
        System.out.println("   Emisor (mensaje): " + mensaje.getEmisor());
        System.out.println("   Receptor (mensaje): " + mensaje.getReceptor());

        Usuario emisor = servicioUsuario.buscarPorNombreUsuario(mensaje.getEmisor());
        Usuario receptor = servicioUsuario.buscarPorNombreUsuario(mensaje.getReceptor());

        if (emisor == null || receptor == null) {
            System.out.println(" Emisor o receptor no encontrado en la base de datos.");
            return;
        }

        System.out.println("   Emisor (DB): " + emisor.getNombreUsuario());
        System.out.println("   Receptor (DB): " + receptor.getNombreUsuario());

        List<Usuario> amigos = servicioUsuario.obtenerAmigos(emisor.getId());

        if (!amigos.contains(receptor)) {
            System.out.println(" No son amigos. Mensaje bloqueado.");
            return;
        }

        System.out.println(" Enviando mensaje a: /user/" + receptor.getNombreUsuario() + "/queue/mensajes");

        messagingTemplate.convertAndSendToUser(
                receptor.getNombreUsuario(),
                "/queue/mensajes",
                mensaje
        );
    }

}
