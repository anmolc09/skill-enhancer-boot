package com.learning.entity.document;

import com.learning.enums.BatchStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "batch")
public class BatchDocument {

	@Id
	private Long id;
	private Integer studentCount;
	private LocalDate startDate;
	private LocalDate endDate;
	@Enumerated(value = EnumType.STRING)
	private BatchStatus batchStatus;
	private Long courseId;
	private Long timeslotId;

}
