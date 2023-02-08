package com.learning.entity.collections;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "course")
public class CourseCollection {

    @Id
    private Long id;
    private String name;
    private String curriculum;
    private String duration;

}
