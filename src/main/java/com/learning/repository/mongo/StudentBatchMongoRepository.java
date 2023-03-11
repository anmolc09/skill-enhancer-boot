package com.learning.repository.mongo;

import com.learning.entity.document.StudentBatchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentBatchMongoRepository extends MongoRepository<StudentBatchDocument, Long> {

}
