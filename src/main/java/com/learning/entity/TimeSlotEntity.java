package com.learning.entity;

import java.time.LocalTime;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "timeslot")
public class TimeSlotEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long trainerId;

}
