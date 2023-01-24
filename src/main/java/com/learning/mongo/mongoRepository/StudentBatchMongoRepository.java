package com.learning.mongo.mongoRepository;

import com.learning.mongo.collections.StudentBatchCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentBatchMongoRepository extends MongoRepository<StudentBatchCollection, Long> {

}
