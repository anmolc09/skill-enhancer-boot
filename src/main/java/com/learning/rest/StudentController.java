package com.learning.rest;

import java.util.*;

import com.learning.entity.StudentEntity;
import com.learning.models.StudentModel;
import com.learning.service.StudentService;
import com.learning.service.mysql.StudentMySqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentMySqlService studentService;
    private final StudentService service;

    @GetMapping("/{id}")
    public StudentModel getRecordById(@PathVariable Long id) {
        return studentService.getRecordById(id);
    }

    @GetMapping("get-records")
    public List<StudentModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count
            , @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        return studentService.getAllRecords(count, sortBy);
    }

    public List<StudentModel> getAllRecords(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {

        return service.getAllRecordByPaginationAndSorting(page, limit, sortBy);
    }

    @PostMapping
    public List<StudentModel> saveRecords(@RequestBody List<StudentModel> studentModelList) {
        return studentService.saveRecords(studentModelList);
    }

    @PutMapping("/{id}")
    public StudentModel updateRecordById(@PathVariable Long id, @RequestBody StudentModel studentModel) {
        return studentService.updateRecord(id, studentModel);
    }

    @DeleteMapping("/{id}")
    public void deleteRecordById(@PathVariable Long id) {
        studentService.deleteRecordById(id);
    }

    @PostMapping("/upload")
    public void uploadExcelFile(@RequestParam("file") MultipartFile file) {
        studentService.saveExcelFile(file);
    }

}
