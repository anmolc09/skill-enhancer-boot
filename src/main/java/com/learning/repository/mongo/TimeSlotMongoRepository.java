package com.learning.repository.mongo;

import com.learning.entity.document.TimeSlotDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeSlotMongoRepository extends MongoRepository<TimeSlotDocument, Long> {

}
