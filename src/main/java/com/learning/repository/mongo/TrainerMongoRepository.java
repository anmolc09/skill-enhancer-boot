package com.learning.repository.mongo;

import com.learning.entity.document.TrainerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainerMongoRepository extends MongoRepository<TrainerDocument, Long> {

}
