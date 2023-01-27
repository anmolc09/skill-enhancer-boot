package com.learning.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.models.BatchModel;
import com.learning.models.StudentBatchModel;
import com.learning.service.StudentBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
	public List<StudentBatchModel> saveRecord(@RequestBody List<StudentBatchModel> studentBatchModelList) {
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

	/*@GetMapping
	public List<StudentBatchModel> getAllRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false,defaultValue = "") String sortBy) {
		if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
			return studentBatchService.getAllRecords();
		} else if (count > 0) {
			return studentBatchService.getLimitedRecords(count);
		} else {
			return studentBatchService.getSortedRecords(sortBy);
		}
	}*/

