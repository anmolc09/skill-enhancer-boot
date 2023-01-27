package com.learning.repository.mongo;

import com.learning.entity.collections.StudentBatchCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentBatchMongoRepository extends MongoRepository<StudentBatchCollection, Long> {

}
