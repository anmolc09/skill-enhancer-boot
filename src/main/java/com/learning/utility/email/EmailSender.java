package com.learning.utility.email;


import com.learning.config.EmailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final EmailConfig emailConfigs;

    public void mailSenderThread(List<String> emailList) {

        Runnable runnable = () -> {

            try {
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

}
