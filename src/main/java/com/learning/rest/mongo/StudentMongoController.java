package com.learning.rest.mongo;

import com.learning.models.StudentModel;
import com.learning.service.mongo.StudentMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("student-mongo")
@RequiredArgsConstructor
public class StudentMongoController {

    private final StudentMongoService studentService;

    @GetMapping("/{id}")
    public StudentModel getRecordById(@PathVariable Long id) {
        return studentService.getRecordById(id);
    }

    @GetMapping("get-records")
    public List<StudentModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return studentService.getAllRecords();
        } else if (count > 0) {
            return studentService.getLimitedRecords(count);
        } else {
            return studentService.getSortedRecords(sortBy);
        }
    }

    @PostMapping
    public List<StudentModel> save(@RequestBody List<StudentModel> studentModelList) {
        try {
            if (studentModelList.size() == 1) {
                return Arrays.asList(studentService.saveRecord(studentModelList.get(0)));
            } else {
                return studentService.saveAll(studentModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }

    @PutMapping("/{id}")
    public StudentModel updateRecordById(@PathVariable Long id, @RequestBody StudentModel studentModel) {
        return studentService.updateRecord(id, studentModel);
    }

    @DeleteMapping("/{id}")
    public void deleteRecordById(@PathVariable Long id) {
        studentService.deleteRecordById(id);
    }


}
