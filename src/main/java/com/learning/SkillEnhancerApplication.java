package com.learning;

import com.learning.repository.StudentRepository;
import com.learning.utility.email.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SkillEnhancerApplication {

    @Autowired
    private EmailSender emailSender;

    public static void main(String[] args) {
        SpringApplication.run(SkillEnhancerApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendEmail() {
        String message = "  EMAIL TESTER RUNNING  ";
        String subject = "email_sender in spring using thread";
        String from = "chawlaanmol789@gmail.com";

        emailSender.mailSenderThread(from, subject, message);
    }

}
