package com.learning.mongo.collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "student")
public class StudentCollection {

	@Id
	private Long id;
	private String name;
	private Long contactDetails;
	private String qualification;
	private String email;

}
