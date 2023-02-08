package com.learning.entity;

import java.time.LocalDate;

import com.learning.enums.BatchStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name ="batch")
public class BatchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer studentCount;
	private LocalDate startDate;
	private LocalDate endDate;
	@Enumerated(value = EnumType.STRING)
	private BatchStatus batchStatus;
	private Long courseId;
	private Long timeslotId;

}
