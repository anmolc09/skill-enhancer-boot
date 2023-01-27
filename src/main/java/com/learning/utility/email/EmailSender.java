package com.learning.utility.email;


import com.learning.config.EmailConfig;
import com.learning.repository.mysql.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final EmailConfig emailConfigs;

    public void mailSenderThread() {

        Runnable runnable = () -> {

            try {
                List<String> emailList = studentRepository.findEmails();
                log.info("Sending Emails...");
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

                for (String email : emailList) {
                    simpleMailMessage.setFrom(emailConfigs.getFrom());
                    simpleMailMessage.setTo(email);
                    simpleMailMessage.setSubject(emailConfigs.getSubject());
                    simpleMailMessage.setText(emailConfigs.getMessage());

                    mailSender.send(simpleMailMessage);
                    log.info(String.format("Mail sent to %s", email));
                }
                log.info(" All Mails Sent Successfully...");
            } catch (Exception e) {
                log.error("Error while sending mails");
            }

        };
        CompletableFuture.runAsync(runnable);
    }


    /*    @EventListener(ApplicationReadyEvent.class)
    public void mailSenderThread() {

        Runnable runnable = () -> {
            try {
                log.info("Sending Emails...");
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

                simpleMailMessage.setFrom(emailConfigs.getFrom());
                simpleMailMessage.setTo(emailConfigs.getSendTo());
                simpleMailMessage.setSubject(emailConfigs.getSubject());
                simpleMailMessage.setText(emailConfigs.getMessage());

                mailSender.send(simpleMailMessage);
                log.info(String.format("Mail sent to %s", emailConfigs.getSendTo()));
                log.info(" Mail Sent Successfully...");
            }catch (Exception e){
                log.error("Error while sending mails");
            }
        };
        CompletableFuture.runAsync(runnable);
    }*/
}
