package com.learning.service.mongo;

import com.learning.constants.NumberConstant;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.BatchModel;
import com.learning.entity.collections.BatchCollection;
import com.learning.repository.mongo.BatchMongoRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchMongoService implements CommonService<BatchModel, Long> {

    private final BatchMongoRepository batchRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<BatchModel> getAllRecords() {
        List<BatchCollection> batchCollectionList = batchRepo.findAll();
        if (Objects.nonNull(batchCollectionList) && batchCollectionList.size() > NumberConstant.ZERO) {
            return batchCollectionList.stream().map(batchCollection -> {
                BatchModel batchModel = modelMapper.map(batchCollection, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BatchModel> getLimitedRecords(int count) {
        List<BatchCollection> batchCollectionList = batchRepo.findAll();
        if (Objects.nonNull(batchCollectionList) && batchCollectionList.size() > NumberConstant.ZERO) {
            return batchCollectionList.stream().limit(count).map(batchCollection -> {
                BatchModel batchModel = modelMapper.map(batchCollection, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BatchModel> getSortedRecords(String sortBy) {
        List<BatchCollection> batchCollectionList = batchRepo.findAll();
        Comparator<BatchCollection> comparator = findSuitableComparator(sortBy);
        if (Objects.nonNull(batchCollectionList) && batchCollectionList.size() > NumberConstant.ZERO) {
            return batchCollectionList.stream().sorted(comparator).map(batchCollection -> {
                BatchModel batchModel = modelMapper.map(batchCollection, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public BatchModel saveRecord(BatchModel record) {
        if (Objects.nonNull(record)) {
            BatchCollection entity = modelMapper.map(record, BatchCollection.class);
            batchRepo.save(entity);
        }
        return record;
    }

    @Override
    public List<BatchModel> saveAll(List<BatchModel> batchModelList) {
        if (Objects.nonNull(batchModelList) && batchModelList.size() > NumberConstant.ZERO) {
            List<BatchCollection> batchCollectionList = batchModelList.stream().map(batchModel -> {
                BatchCollection entity = modelMapper.map(batchModel, BatchCollection.class);
                return entity;
            }).collect(Collectors.toList());
            batchRepo.saveAll(batchCollectionList);
        }
        return batchModelList;
    }

    @Override
    public BatchModel getRecordById(Long id) {
        BatchCollection batchCollection = batchRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        BatchModel batchModel = modelMapper.map(batchCollection, BatchModel.class);
        return batchModel;
    }

    @Override
    public BatchModel updateRecord(Long id, BatchModel record) {
        BatchCollection batchCollection = batchRepo.findById(id).orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, batchCollection);
            batchRepo.save(batchCollection);
            return record;
        }

    @Override
    public void deleteRecordById(Long id) {
        batchRepo.deleteById(id);
    }

    private Comparator<BatchCollection> findSuitableComparator(String sortBy) {
        Comparator<BatchCollection> comparator;
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
