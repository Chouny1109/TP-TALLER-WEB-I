//package com.tallerwebi.integracion.config;
//
//import com.tallerwebi.config.SpringWebConfig;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("prod")
//@ContextConfiguration(classes = {SpringWebConfig.class}) // tu configuración real con javaMailSender bean
//public class MailSenderProdTest {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Test
//    public void deberiaEnviarMailReal() {
//        assertNotNull(javaMailSender, "JavaMailSender no debe ser nulo");
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("preguntadostallerweb@gmail.com");
//        message.setTo("giulianadirocco1@gmail.com");
//        message.setSubject("Correo real de prueba");
//        message.setText("Este correo se envía usando JavaMailSender con perfil PROD.");
//
//        javaMailSender.send(message);
//
//        System.out.println("Correo enviado!");
//    }
//}
