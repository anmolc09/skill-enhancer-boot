package com.learning.models;

import com.learning.enums.BatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BatchModel {

	private Long id;
	private Integer studentCount;
	private LocalDate startDate;
	private LocalDate endDate;
	private BatchStatus batchStatus;
	private Long courseId;
	private Long timeslotId;
	
	}
