package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.BatchEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.BatchModel;
import com.learning.repository.mysql.BatchRepository;
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
public class BatchService {

    private final BatchRepository jpaRepo;
    private final ModelMapper modelMapper;


    public List<BatchModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        return Collections.emptyList();
    }

    public List<BatchModel> saveRecords(List<BatchModel> batchModelList) {
        if (Objects.nonNull(batchModelList) && batchModelList.size() > NumberConstant.ZERO) {
            List<BatchEntity> batchEntityList = batchModelList.stream().map(batchModel -> {
                BatchEntity entity = modelMapper.map(batchModel, BatchEntity.class);
                return entity;
            }).collect(Collectors.toList());
            jpaRepo.saveAll(batchEntityList);
        }
        return batchModelList;
    }

    public BatchModel getRecordById(Long id) {
        BatchEntity batchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
        return batchModel;
    }

    public BatchModel updateRecord(Long id, BatchModel record) {
        BatchEntity batchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, batchEntity);
        jpaRepo.save(batchEntity);
        return record;
    }

    public void deleteRecordById(Long id) {
        if(jpaRepo.existsById(id)){
            jpaRepo.deleteById(id);
            // TODO: add info messages
            log.info("deleted");
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }

}
