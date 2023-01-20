package com.learning.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotModel {
	
	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long trainerId;
	
	}
