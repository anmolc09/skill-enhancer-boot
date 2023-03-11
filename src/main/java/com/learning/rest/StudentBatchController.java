package com.learning.rest;

import com.learning.models.StudentBatchModel;
import com.learning.service.StudentBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-batch")
@RequiredArgsConstructor
public class StudentBatchController {
	
	private final StudentBatchService studentBatchService;

	@GetMapping("/{id}")
	public StudentBatchModel getRecordById(@PathVariable Long id) {
		return studentBatchService.getRecordById(id);
	}

	@GetMapping("/get-records")
	public List<StudentBatchModel> getAllRecords(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
		return studentBatchService.getAllRecordByPaginationAndSorting(page, limit, sortBy);
	}

	@PostMapping
	public List<StudentBatchModel> saveRecords(@RequestBody List<StudentBatchModel> studentBatchModelList) {
		return studentBatchService.saveRecords(studentBatchModelList);
	}

	@PutMapping("/{id}")
	public StudentBatchModel updateRecord(@PathVariable Long id,@RequestBody StudentBatchModel record) {
		return studentBatchService.updateRecord(id, record);
	}

	@DeleteMapping("/{id}")
	public void deleteRecordById(@PathVariable Long id) {
		studentBatchService.deleteRecordById(id);
	}

}

