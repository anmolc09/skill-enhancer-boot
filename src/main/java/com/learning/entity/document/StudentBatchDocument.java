package com.learning.entity.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@Document(collection = "student_batch")
public class StudentBatchDocument {

	@Id
	private Long id;
	private Double fees;
	private Long studentId;
	private Long batchId;

}
