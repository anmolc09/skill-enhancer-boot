package com.learning.repository.mongo;

import com.learning.entity.collections.CourseCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseMongoRepository extends MongoRepository<CourseCollection, Long> {

}
