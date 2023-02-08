package com.learning.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.models.TimeSlotModel;
import com.learning.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/time-slot")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

  @GetMapping("/get-records")
  public List<TimeSlotModel> getAllRecords(
          @RequestParam(value = "page", required = false, defaultValue = "0") int page,
          @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
          @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {

      return timeSlotService.getAllRecordByPaginationAndSorting(page, limit, sortBy);
  }

    @PostMapping
    public List<TimeSlotModel> saveRecords(@RequestBody List<TimeSlotModel> timeSlotModelList) {
       return timeSlotService.saveRecords(timeSlotModelList);
    }

    @PutMapping("/{id}")
    public TimeSlotModel updateRecord(@PathVariable Long id,@RequestBody TimeSlotModel record) {
        return timeSlotService.updateRecord(id, record);
    }

    @GetMapping("/{id}")
    public TimeSlotModel getRecordById(@PathVariable Long id) {
        return timeSlotService.getRecordById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRecordById(@PathVariable Long id) {
        timeSlotService.deleteRecordById(id);
    }

}
