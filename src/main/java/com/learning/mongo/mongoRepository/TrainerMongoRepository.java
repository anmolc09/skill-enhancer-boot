package com.learning.mongo.mongoRepository;

import com.learning.mongo.collections.TrainerCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainerMongoRepository extends MongoRepository<TrainerCollection, Long> {

}
