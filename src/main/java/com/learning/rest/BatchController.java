package com.learning.rest;

import com.learning.models.BatchModel;
import com.learning.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

	private final BatchService batchService;

	@GetMapping("/{id}")
	public BatchModel getRecordById(@PathVariable Long id) {
		return batchService.getRecordById(id);
	}

	@GetMapping("/get-records")
	public List<BatchModel> getAllRecords(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
		return batchService.getAllRecordByPaginationAndSorting(page, limit, sortBy);
	}

	@PostMapping
	public List<BatchModel> saveRecords(@RequestBody List<BatchModel> batchModelList) {
		return batchService.saveRecords(batchModelList);
	}

	@PutMapping("/{id}")
	public BatchModel updateRecord(@PathVariable Long id, @RequestBody BatchModel record) {
		return batchService.updateRecord(id, record);
	}

	@DeleteMapping("/{id}")
	public void deleteRecordById(@PathVariable Long id) {
		batchService.deleteRecordById(id);
	}

}
