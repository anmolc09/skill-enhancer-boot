package com.learning.service.mysql;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstant;
import com.learning.entity.BatchEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.BatchModel;
import com.learning.repository.mysql.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchMysqlService {

    private final BatchRepository batchRepo;
    private final ModelMapper modelMapper;

    public List<BatchModel> getAllRecords() {
        List<BatchEntity> batchEntityList = batchRepo.findAll();
        if (Objects.nonNull(batchEntityList) && batchEntityList.size() > NumberConstant.ZERO) {
            return batchEntityList.stream().map(batchEntity -> {
                BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<BatchModel> getLimitedRecords(int count) {
        List<BatchEntity> batchEntityList = batchRepo.findAll();
        if (Objects.nonNull(batchEntityList) && batchEntityList.size() > NumberConstant.ZERO) {
            return batchEntityList.stream().limit(count).map(batchEntity -> {
                BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<BatchModel> getSortedRecords(String sortBy) {
        List<BatchEntity> batchEntityList = batchRepo.findAll();
        Comparator<BatchEntity> comparator = findSuitableComparator(sortBy);
        if (Objects.nonNull(batchEntityList) && batchEntityList.size() > NumberConstant.ZERO) {
            return batchEntityList.stream().sorted(comparator).map(batchEntity -> {
                BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public BatchModel saveRecord(BatchModel record) {
        if (Objects.nonNull(record)) {
            BatchEntity entity = modelMapper.map(record, BatchEntity.class);
            batchRepo.save(entity);
        }
        return record;
    }

    public List<BatchModel> saveAll(List<BatchModel> batchModelList) {
        if (Objects.nonNull(batchModelList) && batchModelList.size() > NumberConstant.ZERO) {
            List<BatchEntity> batchEntityList = batchModelList.stream().map(batchModel -> {
                BatchEntity entity = modelMapper.map(batchModel, BatchEntity.class);
                return entity;
            }).collect(Collectors.toList());
            batchRepo.saveAll(batchEntityList);
        }
        return batchModelList;
    }

    public BatchModel getRecordById(Long id) {
        BatchEntity batchEntity = batchRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
        return batchModel;
    }

    public BatchModel updateRecord(Long id, BatchModel record) {
        BatchEntity batchEntity = batchRepo.findById(id).orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, batchEntity);
            batchRepo.save(batchEntity);
            return record;
        }

    public void deleteRecordById(Long id) {
        batchRepo.deleteById(id);
    }

    private Comparator<BatchEntity> findSuitableComparator(String sortBy) {
        Comparator<BatchEntity> comparator;
        switch (sortBy) {
            case "startDate": {
                comparator = (batchOne, batchTwo) -> batchOne.getStartDate().compareTo(batchTwo.getStartDate());
                break;
            }
            case "endDate": {
                comparator = (batchOne, batchTwo) -> batchOne.getEndDate().compareTo(batchTwo.getEndDate());
                break;
            }
            case "studentCount": {
                comparator = (batchOne, batchTwo) -> batchOne.getStudentCount().compareTo(batchTwo.getStudentCount());
                break;
            }
            default: {
                comparator = (batchOne, batchTwo) -> batchOne.getId().compareTo(batchTwo.getId());
            }
        }
        return comparator;
    }
}
