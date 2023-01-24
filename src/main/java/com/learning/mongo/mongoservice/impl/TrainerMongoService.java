package com.learning.mongo.mongoservice.impl;

import com.learning.constants.NumberConstant;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.TrainerModel;
import com.learning.mongo.collections.TrainerCollection;
import com.learning.mongo.mongoRepository.TrainerMongoRepository;
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
public class TrainerMongoService implements CommonService<TrainerModel, Long> {

    private final TrainerMongoRepository trainerRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<TrainerModel> getAllRecords() {
        List<TrainerCollection> trainerCollectionList = trainerRepo.findAll();
        if (Objects.nonNull(trainerCollectionList) && trainerCollectionList.size() > NumberConstant.ZERO) {
            return trainerCollectionList.stream()
                    .map(trainerCollection -> {
                        TrainerModel trainerModel = modelMapper.map(trainerCollection, TrainerModel.class);
                        return trainerModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TrainerModel> getLimitedRecords(int count) {
        List<TrainerCollection> trainerCollectionList = trainerRepo.findAll();
        if (Objects.nonNull(trainerCollectionList) && trainerCollectionList.size() > NumberConstant.ZERO) {
            return trainerCollectionList.stream()
                    .limit(count)
                    .map(trainerCollection -> {
                        TrainerModel trainerModel = modelMapper.map(trainerCollection, TrainerModel.class);
                        return trainerModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TrainerModel> getSortedRecords(String sortBy) {
        List<TrainerCollection> trainerCollectionList = trainerRepo.findAll();
        if (Objects.nonNull(trainerCollectionList) && trainerCollectionList.size() > NumberConstant.ZERO) {
            Comparator<TrainerCollection> comparator = findSuitableComparator(sortBy);
            return trainerCollectionList.stream()
                    .sorted(comparator)
                    .map(trainerCollection -> {
                        TrainerModel trainerModel = modelMapper.map(trainerCollection, TrainerModel.class);
                        return trainerModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public TrainerModel saveRecord(TrainerModel trainerModel) {
        if (Objects.nonNull(trainerModel)) {
            TrainerCollection entity = modelMapper.map(trainerModel, TrainerCollection.class);
            trainerRepo.save(entity);
        }
        return trainerModel;
    }

    @Override
    public List<TrainerModel> saveAll(List<TrainerModel> trainerModelList) {
        if (Objects.nonNull(trainerModelList) && trainerModelList.size() > NumberConstant.ZERO) {
            List<TrainerCollection> trainerCollectionList = trainerModelList.stream()
                    .map(trainerModel -> {
                        TrainerCollection entity = modelMapper.map(trainerModel, TrainerCollection.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            trainerRepo.saveAll(trainerCollectionList);
        }
        return trainerModelList;
    }

    @Override
    public TrainerModel getRecordById(Long id) {
        TrainerCollection trainerCollection = trainerRepo.findById(id)
				.orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        TrainerModel trainerModel = modelMapper.map(trainerCollection, TrainerModel.class);
        return trainerModel;
    }

    @Override
    public TrainerModel updateRecord(Long id, TrainerModel record) {
        TrainerCollection trainerCollection = trainerRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record,trainerCollection);
        trainerRepo.save(trainerCollection);
        return  record;
    }

    @Override
    public void deleteRecordById(Long id) {
        trainerRepo.deleteById(id);
    }

    private Comparator<TrainerCollection> findSuitableComparator(String sortBy) {
        Comparator<TrainerCollection> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (trainerOne, trainerTwo) ->
                        trainerOne.getName().compareTo(trainerTwo.getName());
                break;
            }
            case "specialization": {
                comparator = (trainerOne, trainerTwo) ->
                        trainerOne.getSpecialisation().compareTo(trainerTwo.getSpecialisation());
                break;
            }
            default: {
                comparator = (trainerOne, trainerTwo) ->
                        trainerOne.getId().compareTo(trainerTwo.getId());
            }
        }
        return comparator;
    }

}
