package com.learning.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.models.TrainerModel;
import com.learning.service.mysql.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/{id}")
    public TrainerModel getRecordById(@PathVariable Long id) {
        return trainerService.getRecordById(id);
    }

    @GetMapping
    public List<TrainerModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return trainerService.getAllRecords();
        } else if (count > 0) {
            return trainerService.getLimitedRecords(count);
        } else {
            return trainerService.getSortedRecords(sortBy);
        }

    }

    @PostMapping
    public List<TrainerModel> save(@RequestBody List<TrainerModel> trainerModelList) {
        try {
            if (trainerModelList.size() == 1) {
                return Arrays.asList(trainerService.saveRecord(trainerModelList.get(0)));
            } else {
                return trainerService.saveAll(trainerModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in TrainerMongoController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }

    @PutMapping("/{id}")
    public TrainerModel updateRecord(@PathVariable Long id,@RequestBody TrainerModel record) {
        return trainerService.updateRecord(id, record);
    }

    @DeleteMapping("/{id}")
    public void deleteRecordById(@PathVariable Long id) {
        trainerService.deleteRecordById(id);
    }

}
