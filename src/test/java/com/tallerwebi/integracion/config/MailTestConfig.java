package com.tallerwebi.integracion.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@Profile("test")
public class MailTestConfig {


    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) {
                System.out.println("[TEST] Simulando envío de mail a: " + simpleMessage.getTo()[0]);
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                for (SimpleMailMessage msg : simpleMessages) {
                    send(msg);
                }
            }

            // Los demás métodos no se usan en tests
            @Override public MimeMessage createMimeMessage() { throw new UnsupportedOperationException(); }
            @Override public MimeMessage createMimeMessage(InputStream contentStream) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessage mimeMessage) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessage... mimeMessages) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessagePreparator mimeMessagePreparator) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessagePreparator... mimeMessagePreparators) { throw new UnsupportedOperationException(); }
        };
    }

    @Bean
    public javax.mail.Session mailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        return javax.mail.Session.getInstance(props);
    }
}

