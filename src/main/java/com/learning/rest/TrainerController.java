package com.learning.rest;

import com.learning.models.TrainerModel;
import com.learning.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/{id}")
    public TrainerModel getRecordById(@PathVariable Long id) {
        return trainerService.getRecordById(id);
    }

    @GetMapping("/get-records")
    public List<TrainerModel> getAllRecords(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {

        return trainerService.getAllRecordByPaginationAndSorting(page, limit, sortBy);
    }

    @PostMapping
    public List<TrainerModel> saveRecords(@RequestBody List<TrainerModel> trainerModelList) {
        return trainerService.saveRecords(trainerModelList);
    }

    @PutMapping("/{id}")
    public TrainerModel updateRecord(@PathVariable Long id, @RequestBody TrainerModel record) {
        return trainerService.updateRecord(id, record);
    }

    @DeleteMapping("/{id}")
    public void deleteRecordById(@PathVariable Long id) {
        trainerService.deleteRecordById(id);
    }

}