package com.learning.entity.collections;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@Document(collection = "student_batch")
public class StudentBatchCollection {

	@Id
	private Long id;
	private Double fees;
	private Long studentId;
	private Long batchId;

}
