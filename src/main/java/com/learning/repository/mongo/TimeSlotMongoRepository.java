package com.learning.repository.mongo;

import com.learning.collections.TimeSlotCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeSlotMongoRepository extends MongoRepository<TimeSlotCollection, Long> {

}
