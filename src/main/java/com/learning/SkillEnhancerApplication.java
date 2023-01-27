package com.learning;

import com.learning.entity.StudentEntity;
import com.learning.models.StudentModel;
import com.learning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;


@SpringBootApplication
public class SkillEnhancerApplication implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(SkillEnhancerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        Page<StudentEntity> studentEntities = studentService.getAllRecordByPaginationAndSorting(0, 5, "name");
//        System.out.println(studentEntities);
    }
}
