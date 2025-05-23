package com.tallerwebi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
@Configuration
@Profile("dev")
public class MailDevConfig {

    @Bean
    public JavaMailSender mailSender() {
        return new JavaMailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) {
                System.out.println("Mock mail enviado a: " + simpleMessage.getTo()[0]);
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                for (SimpleMailMessage msg : simpleMessages) {
                    send(msg);
                }
            }

            @Override public MimeMessage createMimeMessage() { throw new UnsupportedOperationException(); }
            @Override public MimeMessage createMimeMessage(InputStream contentStream) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessage mimeMessage) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessage... mimeMessages) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessagePreparator mimeMessagePreparator) { throw new UnsupportedOperationException(); }
            @Override public void send(MimeMessagePreparator... mimeMessagePreparators) { throw new UnsupportedOperationException(); }
        };
    }
}
