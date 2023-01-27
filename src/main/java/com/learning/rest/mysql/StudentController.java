package com.learning.rest.mysql;

import java.util.*;

import com.learning.models.StudentModel;
import com.learning.service.mysql.StudentMySqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

	private final StudentMySqlService studentService;

	@GetMapping("/{id}")
	public StudentModel getRecordById(@PathVariable Long id) {
		return studentService.getRecordById(id);
	}

	@GetMapping("get-records")
	public List<StudentModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count
			, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
		return studentService.getAllRecords(count, sortBy);
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
