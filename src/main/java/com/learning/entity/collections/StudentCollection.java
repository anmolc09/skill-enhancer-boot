package com.learning.entity.collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@Document(collection = "student")
public class StudentCollection {

	@Id
	private Long id;
	private String name;
	private Long contactDetails;
	private String qualification;
	private String email;

}
