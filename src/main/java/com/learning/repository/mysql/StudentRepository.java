package com.learning.repository.mysql;

import com.learning.entity.StudentEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {


    @Query(value = "select email from student" ,nativeQuery = true)
    List<String> findEmails();


}
