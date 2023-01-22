package com.learning.repository;

import com.learning.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    @Query("select email from student")
    List<String> findEmails();

}
