package com.learning.mongo.mongoRepository;

import com.learning.mongo.collections.CourseCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseMongoRepository extends MongoRepository<CourseCollection, Long> {

}
