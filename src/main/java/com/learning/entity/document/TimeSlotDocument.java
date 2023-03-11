package com.learning.entity.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalTime;


@Getter
@Setter
@Document(collection = "timeslot")
public class TimeSlotDocument {
	
	@Id
	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long trainerId;

}
