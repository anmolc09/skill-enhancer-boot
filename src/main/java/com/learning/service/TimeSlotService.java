package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.TimeSlotEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.TimeSlotModel;
import com.learning.repository.mysql.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeSlotService {

    private final TimeSlotRepository jpaRepo;
    private final ModelMapper modelMapper;

    public List<TimeSlotModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        return Collections.emptyList();
    }

    public List<TimeSlotModel> saveRecords(List<TimeSlotModel> timeSlotModelList) {
        if (Objects.nonNull(timeSlotModelList) && timeSlotModelList.size() > NumberConstant.ZERO) {
            List<TimeSlotEntity> timeSlotEntityList = timeSlotModelList.stream().map(timeSlotModel -> {
                TimeSlotEntity entity = modelMapper.map(timeSlotModel, TimeSlotEntity.class);
                return entity;
            }).collect(Collectors.toList());
            jpaRepo.saveAll(timeSlotEntityList);
        }
        return timeSlotModelList;
    }

    public TimeSlotModel getRecordById(Long id) {
        TimeSlotEntity timeSlotEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotEntity, TimeSlotModel.class);
        return timeSlotModel;
    }

    public TimeSlotModel updateRecord(Long id, TimeSlotModel record) {
        TimeSlotEntity timeSlotEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, timeSlotEntity);
        jpaRepo.save(timeSlotEntity);
        return record;
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            jpaRepo.deleteById(id);
            log.info("deleted");
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }
}
