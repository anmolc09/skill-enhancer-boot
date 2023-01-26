package com.learning.mongo.mongoRepository;

import com.learning.mongo.collections.StudentCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface StudentMongoRepository extends MongoRepository<StudentCollection, Long> {

}
