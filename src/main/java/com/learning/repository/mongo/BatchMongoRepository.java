package com.learning.repository.mongo;

import com.learning.entity.document.BatchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatchMongoRepository extends MongoRepository<BatchDocument, Long> {

}
