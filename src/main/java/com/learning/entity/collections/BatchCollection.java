package com.learning.entity.collections;

import com.learning.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "batch")
public class BatchCollection {

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
