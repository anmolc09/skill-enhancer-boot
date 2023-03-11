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

    @GetMapping("/get-records")
    public List<CourseModel> getAllRecords(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {
        return courseService.getAllRecordByPaginationAndSorting(page, limit, sortBy);
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
