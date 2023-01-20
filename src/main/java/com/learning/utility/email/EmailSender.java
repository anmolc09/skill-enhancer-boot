/*
package com.learning.utility.email;

import com.learning.entity.StudentEntity;
import com.learning.utility.excel.reader.StudentReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

    private final StudentReader studentReader;

    public void chainingMethod(){

        List<StudentEntity> studentEntityList = studentReader.getStudentObjects();
        List<String> emails = studentEntityList.stream()
                .map(studentEntity -> studentEntity.getEmail()).collect(Collectors.toList());
        System.out.println(emails);


    }

}
*/
