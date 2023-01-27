package com.learning.rest.mysql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.models.BatchModel;
import com.learning.service.mysql.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

	private final BatchService batchService;

	@GetMapping("/{id}")
	public BatchModel getRecordById(@PathVariable Long id) {
		return batchService.getRecordById(id);
	}

	@GetMapping
	public List<BatchModel> getAllRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
		if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
			return batchService.getAllRecords();
		} else if (count > 0) {
			return batchService.getLimitedRecords(count);
		} else {
			return batchService.getSortedRecords(sortBy);
		}
	}

	@PostMapping
	public List<BatchModel> save(@RequestBody List<BatchModel> batchModelList) {
		try {
			if (batchModelList.size() == 1) {
				return Arrays.asList(batchService.saveRecord(batchModelList.get(0)));
			} else {
				return batchService.saveAll(batchModelList);
			}
		} catch (Exception exception) {
			System.out.println("Exception Occurs in BatchController || saveAll");
			System.err.print(exception);
			return Collections.emptyList();
		}
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
