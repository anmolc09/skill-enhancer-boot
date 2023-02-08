package com.learning.rest;

import com.learning.models.CourseModel;
import com.learning.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/{id}")
    public CourseModel getRecordById(@PathVariable Long id) {
        return courseService.getRecordById(id);
    }

    @PostMapping
    public List<CourseModel> saveRecords(@RequestBody List<CourseModel> courseModelList) {
        return courseService.saveRecords(courseModelList);
    }

    @PutMapping("/{id}")
    public CourseModel updateRecord(@PathVariable Long id, @RequestBody CourseModel record) {
        return courseService.updateRecord(id, record);
    }

    @DeleteMapping("/{id}")
    public void deleteRecordById(@PathVariable Long id) {
        courseService.deleteRecordById(id);
    }

}
