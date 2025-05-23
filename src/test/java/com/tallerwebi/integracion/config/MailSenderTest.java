package com.tallerwebi.integracion.config;
import com.tallerwebi.integracion.config.MailTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {MailTestConfig.class})
public class MailSenderTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void deberiaUsarMailSenderDeTest() {
        assertNotNull(javaMailSender, "JavaMailSender no debe ser nulo");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("test@example.com");
        message.setSubject("Test");
        message.setText("Este es un mensaje de prueba");

        // Solo para verificar que no lance excepción
        javaMailSender.send(message);
    }
}
