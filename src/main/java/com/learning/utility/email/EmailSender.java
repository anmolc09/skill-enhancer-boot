package com.learning.utility.email;


import com.learning.models.StudentModel;
import com.learning.repository.StudentRepository;
import com.learning.service.impl.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;
    private final StudentRepository studentRepository;

    public void sendEmail(String message, String subject, String to, String from){

        System.out.println("Sending Emails...");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        mailSender.send(simpleMailMessage);

        System.out.println("Mail Sent Successfully...");
    }

    public void mailSenderThread(String from,String subject, String message) {
        Runnable runnable = () -> {
            List<String> emailList = studentRepository.findEmails();
            //List<String> emailList = studentRepository.findAll().stream().map(studentModel -> studentModel.getEmail()).collect(Collectors.toList());

            System.out.println("Sending Emails...");
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            for(String email : emailList) {
                simpleMailMessage.setFrom(from);
                simpleMailMessage.setTo(email);
                System.out.println(String.format("Mail sent to %s",email));
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(message);

                mailSender.send(simpleMailMessage);
            }
            System.out.println(" All Mails Sent Successfully...");
        };
        CompletableFuture.runAsync(runnable);
    }
}
