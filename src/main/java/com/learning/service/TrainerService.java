package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.TrainerEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.TrainerModel;
import com.learning.repository.mysql.TrainerRepository;
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
public class TrainerService {

    private final TrainerRepository jpaRepo;
    private final ModelMapper modelMapper;
    public List<TrainerModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        return Collections.emptyList();
    }

    public List<TrainerModel> saveRecords(List<TrainerModel> trainerModelList) {
        if (Objects.nonNull(trainerModelList) && trainerModelList.size() > NumberConstant.ZERO) {
            List<TrainerEntity> trainerEntityList = trainerModelList.stream()
                    .map(trainerModel -> {
                        TrainerEntity entity = modelMapper.map(trainerModel, TrainerEntity.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            jpaRepo.saveAll(trainerEntityList);
        }
        return trainerModelList;
    }

    public TrainerModel getRecordById(Long id) {
        TrainerEntity trainerEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
        return trainerModel;
    }

    public TrainerModel updateRecord(Long id, TrainerModel record) {
        TrainerEntity trainerEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, trainerEntity);
        jpaRepo.save(trainerEntity);
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
