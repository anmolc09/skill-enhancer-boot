package com.learning.collections;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "course")
public class CourseCollection {

    @Id
    private Long id;
    private String name;
    private String curriculum;
    private String duration;

}
