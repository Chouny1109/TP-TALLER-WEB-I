//package com.tallerwebi.config;
//
//import java.io.IOException;
//import java.util.Properties;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PropertiesLoaderUtils;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.context.annotation.PropertySource;
//import java.util.Properties;
//@Configuration
//@Profile("prod")
//public class MailProdConfig {
//
//
//    @Value("${mail.host}")
//    private String host;
//
//    @Value("${mail.port}")
//    private int port;
//
//    @Value("${mail.username}")
//    private String username;
//
//    @Value("${mail.password}")
//    private String password;
//
//    @Value("${mail.protocol}")
//    private String protocol;
//
//    @Value("${mail.smtp.auth}")
//    private boolean smtpAuth;
//
//    @Value("${mail.smtp.starttls.enable}")
//    private boolean startTls;
//
//    @Value("${mail.smtp.socketFactory.port}")
//    private int socketFactoryPort;
//
//    @Value("${mail.smtp.socketFactory.class}")
//    private String socketFactoryClass;
//
//    @Value("${mail.smtp.socketFactory.fallback}")
//    private boolean socketFactoryFallback;
//
//    @Value("${mail.smtp.ssl.enable}")
//    private boolean sslEnable;
//
//    @Value("${mail.debug}")
//    private boolean debug;
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//
//        mailSender.setHost(host);
//        mailSender.setPort(port);
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//        mailSender.setProtocol(protocol);
//
//        Properties javaMailProps = new Properties();
//        javaMailProps.put("mail.smtp.auth", String.valueOf(smtpAuth));
//        javaMailProps.put("mail.smtp.starttls.enable", String.valueOf(startTls));
//        javaMailProps.put("mail.smtp.socketFactory.port", String.valueOf(socketFactoryPort));
//        javaMailProps.put("mail.smtp.socketFactory.class", socketFactoryClass);
//        javaMailProps.put("mail.smtp.socketFactory.fallback", String.valueOf(socketFactoryFallback));
//        javaMailProps.put("mail.smtp.ssl.enable", String.valueOf(sslEnable));
//        javaMailProps.put("mail.debug", String.valueOf(debug));
//
//        mailSender.setJavaMailProperties(javaMailProps);
//
//        return mailSender;
//    }
//}
