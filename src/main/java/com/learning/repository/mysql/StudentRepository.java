package com.learning.repository.mysql;

import com.learning.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {


    @Query(value = "select e.email from StudentEntity e" )
    List<String> findEmails();

    @Query(value = "select s.contactDetails from StudentEntity s")
    List<Long> findAllContacts();
/*
@Query(value = "select email from student" ,nativeQuery = true)
    List<String> findEmails();
*/

}
