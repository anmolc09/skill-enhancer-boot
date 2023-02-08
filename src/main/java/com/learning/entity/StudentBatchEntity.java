package com.learning.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Student_batch")
public class StudentBatchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double fees;
	private Long studentId;
	private Long batchId;

}
