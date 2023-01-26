package com.learning.utility.email;


import com.learning.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

    private final JavaMailSender mailSender;
    private final StudentRepository studentRepository;
    public static final String MESSAGE = " EMAIL TESTER RUNNING  DATED - 26/01/23  ";
    public static final String SUBJECT = "email_sender in spring using thread";
    public static final String EMAIL = "anmolc09@gmail.com";

    @Value("${spring.mail.username}")
    private String from;

    @EventListener(ApplicationReadyEvent.class)
    public void mailSenderThread() {

        Runnable runnable = () -> {

            try {
                List<String> emailList = studentRepository.findEmails();
                log.info("Sending Emails...");
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

                for (String email : emailList) {
                    simpleMailMessage.setFrom(from);
                    simpleMailMessage.setTo(email);
                    simpleMailMessage.setSubject(SUBJECT);
                    simpleMailMessage.setText(MESSAGE);

                    mailSender.send(simpleMailMessage);
                    System.out.println(String.format("Mail sent to %s", email));
                }
                log.info(" All Mails Sent Successfully...");
            } catch (Exception e) {
                log.error("Error while sending mails");
            }

        };
        CompletableFuture.runAsync(runnable);
    }

    /*
    public void mailSenderThread(String from, String subject, String message) {

        List<String> emailList = studentRepository.findEmails();

        Runnable runnable = () -> {
        try {
            log.info("Sending Emails...");
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            for (String email : emailList) {
                simpleMailMessage.setFrom(from);
                simpleMailMessage.setTo(email);
                System.out.println(String.format("Mail sent to %s", email));
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(message);

                mailSender.send(simpleMailMessage);
            }
            log.info(" All Mails Sent Successfully...");
        }catch (Exception e){
                log.error("Error while sending mails");
        }
        };
        CompletableFuture.runAsync(runnable);
    }*/
}
