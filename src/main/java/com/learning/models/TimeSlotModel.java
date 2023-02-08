package com.learning.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TimeSlotModel {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long trainerId;

}
