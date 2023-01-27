package com.learning.rest;

import com.learning.models.CourseModel;
import com.learning.service.mysql.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
	
	private final CourseService courseService;

	@GetMapping("/{id}")
	public CourseModel getRecordById(@PathVariable Long id) {
		return courseService.getRecordById(id);
	}

	@GetMapping
	public List<CourseModel> getAllRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count,@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
		if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
			return courseService.getAllRecords();
		} else if (count > 0) {
			return courseService.getLimitedRecords(count);
		} else {
			return courseService.getSortedRecords(sortBy);
		}
	}
	@PostMapping
	public List<CourseModel> save(@RequestBody List<CourseModel> courseModelList) {
		try {
			if (courseModelList.size() == 1) {
				return Arrays.asList(courseService.saveRecord(courseModelList.get(0)));
			} else {
				return courseService.saveAll(courseModelList);
			}
		} catch (Exception exception) {
			System.out.println("Exception Occurs in CourseMongoController || saveAll");
			System.err.print(exception);
			return Collections.emptyList();
		}
	}

	@PutMapping("/{id}")
	public CourseModel updateRecord(@PathVariable Long id,@RequestBody CourseModel record) {
		return courseService.updateRecord(id, record);
	}

	@DeleteMapping("/{id}")
	public void deleteRecordById(@PathVariable Long id) {
		courseService.deleteRecordById(id);
	}

}
