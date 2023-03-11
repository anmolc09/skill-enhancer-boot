package com.learning.repository.mongo;

import com.learning.entity.document.StudentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface StudentMongoRepository extends MongoRepository<StudentDocument, Long> {

}
