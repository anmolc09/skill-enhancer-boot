package com.learning.rest.mysql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.models.TimeSlotModel;
import com.learning.service.mysql.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/time-slot")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @GetMapping
    public List<TimeSlotModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return timeSlotService.getAllRecords();
        } else if (count > 0) {
            return timeSlotService.getLimitedRecords(count);
        } else {
            return timeSlotService.getSortedRecords(sortBy);
        }
    }

    @PostMapping
    public List<TimeSlotModel> save(@RequestBody List<TimeSlotModel> timeSlotModelList) {
        try {
            if (timeSlotModelList.size() == 1) {
                return Arrays.asList(timeSlotService.saveRecord(timeSlotModelList.get(0)));
            } else {
                return timeSlotService.saveAll(timeSlotModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
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
