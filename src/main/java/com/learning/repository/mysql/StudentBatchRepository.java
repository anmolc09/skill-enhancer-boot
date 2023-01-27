package com.learning.repository.mysql;

import com.learning.entity.StudentBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentBatchRepository extends JpaRepository<StudentBatchEntity, Long> {

}
