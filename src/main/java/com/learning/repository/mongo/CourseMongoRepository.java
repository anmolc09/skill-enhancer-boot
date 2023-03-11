package com.learning.repository.mongo;

import com.learning.entity.document.CourseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseMongoRepository extends MongoRepository<CourseDocument, Long> {

}
