package com.learning.repository;

import com.learning.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long> {

}
