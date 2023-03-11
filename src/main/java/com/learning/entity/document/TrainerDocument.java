package com.learning.entity.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@Document(collection = "trainer")
public class TrainerDocument {

	@Id
	private Long id;
	private String name;
	private String specialisation;

}
