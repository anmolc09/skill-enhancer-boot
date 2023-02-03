package com.learning.rest;

import com.learning.models.StudentModel;
import com.learning.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public StudentModel getRecordById(@PathVariable Long id) {
        return studentService.getRecordById(id);
    }

    @GetMapping("/get-records")
    public List<StudentModel> getAllRecords(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {

        return studentService.getAllRecordByPaginationAndSorting(page, limit, sortBy);
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

    @PostMapping("/email")
    public void emailSender() {
        studentService.emailSender();
    }

    @PostMapping("/email/attachment")
    public void sendMailWithAttachment() {
        studentService.sendMailWithAttachment();
    }

    @PostMapping("/transfer")
    public void transferMySqlDataToMongo() {
       studentService.transferMySqlDataToMongo();
    }

}
/*
    @GetMapping("get-records")
    public List<StudentModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count
            , @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        return studentService.getAllRecords(count, sortBy);
    }
*/
