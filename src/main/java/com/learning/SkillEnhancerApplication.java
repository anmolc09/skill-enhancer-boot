package com.learning;

import com.learning.entity.StudentEntity;
import com.learning.models.StudentModel;
import com.learning.repository.mysql.StudentRepository;
import com.learning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;


@SpringBootApplication
public class SkillEnhancerApplication implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(SkillEnhancerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
      //  System.out.println(studentRepository.findEmails());
        System.out.println(studentRepository.findAllContacts());
    }
}
