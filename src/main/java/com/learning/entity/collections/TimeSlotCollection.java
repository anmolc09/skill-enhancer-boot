package com.learning.entity.collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "timeslot")
public class TimeSlotCollection {
	
	@Id
	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long trainerId;

}
