package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordsNotEquals;
import com.tallerwebi.dominio.excepcion.TokenInvalido;
import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.DatosRecovery;
import com.tallerwebi.model.RecoveryToken;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioRecovery;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioRecovery;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Profile("prod")
@Transactional
public class ServicioRecoveryImpl implements ServicioRecovery {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioRecovery repositorioRecovery;
    private JavaMailSender mailSender;
    private PasswordEncoder passwordEncoder;


    public ServicioRecoveryImpl(RepositorioUsuario repositorioUsuario, RepositorioRecovery repositorioRecovery, JavaMailSender mailSender) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioRecovery = repositorioRecovery;
        this.mailSender = mailSender;
        this.passwordEncoder = new BCryptPasswordEncoder();

    }


    @Override
    public Boolean enviarEmailDeRecuperacion(String email) throws UsuarioNoExistente {
        Usuario usuario = repositorioUsuario.buscar(email);
        if (usuario == null) {
            throw new UsuarioNoExistente();
        }

        //Creo token unico
        String token = UUID.randomUUID().toString();

        RecoveryToken recoveryToken = new RecoveryToken(
                token,
                usuario
        );

        this.repositorioRecovery.guardar(recoveryToken);

        // Crea link de recuperacion

        String recoveryLink = "http://localhost:8080/spring/recovery/cambio-de-contrasena?token=" +
                URLEncoder.encode(token, StandardCharsets.UTF_8);


        // 6. Enviar email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Para recuperar tu contraseña, haz click en el siguiente link: " + recoveryLink);
        try {
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();

            return false;
        }


    }

    @Override
    public Usuario cambiarPassword(DatosRecovery datosRecovery, String token) throws PasswordsNotEquals, UsuarioNoExistente, EmailInvalido, TokenInvalido {
        if (!datosRecovery.getPassword().equals(datosRecovery.getConfirmPassword())) {
            throw new PasswordsNotEquals();
        }

        Usuario usuario = repositorioUsuario.buscar(datosRecovery.getEmail());
        if (usuario == null) {
            throw new UsuarioNoExistente();
        }

        System.out.println("Token recibido: '" + token + "'");
        RecoveryToken recoveryToken = this.repositorioRecovery.buscarToken(token);
        System.out.println("Token recuperado: " + (recoveryToken != null ? recoveryToken.getToken() : "null"));


        if (recoveryToken == null || recoveryToken.getUsado()){
            throw new TokenInvalido();
        }
              if( !recoveryToken.getUsuario().getEmail().equals(datosRecovery.getEmail())) {
            throw new EmailInvalido();
        }
        if (recoveryToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new TokenInvalido();  // o crear una excepción específica para token expirado
        }

        usuario.setPassword(passwordEncoder.encode(datosRecovery.getPassword()));
        repositorioUsuario.modificar(usuario);

        recoveryToken.setUsado(true);
        this.repositorioRecovery.actualizar(recoveryToken);

        return usuario;
    }


    @Override
    public RecoveryToken obtenerToken(String token) {
        return this.repositorioRecovery.buscarToken(token);
    }
}
