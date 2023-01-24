package com.learning.mongo.mongoservice.impl;

import com.learning.constants.NumberConstant;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.TimeSlotModel;
import com.learning.mongo.collections.TimeSlotCollection;
import com.learning.mongo.mongoRepository.TimeSlotMongoRepository;
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
public class TimeSlotMongoService implements CommonService<TimeSlotModel, Long> {

    private final TimeSlotMongoRepository timeSlotRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<TimeSlotModel> getAllRecords() {
        List<TimeSlotCollection> timeSlotMongoCollectionList = timeSlotRepo.findAll();
        if (Objects.nonNull(timeSlotMongoCollectionList) && timeSlotMongoCollectionList.size() > NumberConstant.ZERO) {
            return timeSlotMongoCollectionList.stream().map(timeSlotMongoCollection -> {
                TimeSlotModel timeSlotModel = modelMapper.map(timeSlotMongoCollection, TimeSlotModel.class);
                return timeSlotModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TimeSlotModel> getLimitedRecords(int count) {
        List<TimeSlotCollection> timeSlotMongoCollectionList = timeSlotRepo.findAll();
        if (Objects.nonNull(timeSlotMongoCollectionList) && timeSlotMongoCollectionList.size() > NumberConstant.ZERO) {
            return timeSlotMongoCollectionList.stream().limit(count)
                    .map(timeSlotMongoCollection -> {
                        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotMongoCollection, TimeSlotModel.class);
                        return timeSlotModel;
                    }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TimeSlotModel> getSortedRecords(String sortBy) {
        List<TimeSlotCollection> timeSlotMongoCollectionList = timeSlotRepo.findAll();
        if (Objects.nonNull(timeSlotMongoCollectionList) && timeSlotMongoCollectionList.size() > NumberConstant.ZERO) {
            Comparator<TimeSlotCollection> comparator = findSuitableComparator(sortBy);
            return timeSlotMongoCollectionList.stream().sorted(comparator)
                    .map(timeSlotMongoCollection -> {
                        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotMongoCollection, TimeSlotModel.class);
                        return timeSlotModel;
                    }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public TimeSlotModel saveRecord(TimeSlotModel timeSlotModel) {
        if (Objects.nonNull(timeSlotModel)) {
            timeSlotRepo.save(modelMapper.map(timeSlotModel, TimeSlotCollection.class));
        }
        return timeSlotModel;
    }

    @Override
    public List<TimeSlotModel> saveAll(List<TimeSlotModel> timeSlotModelList) {
        if (Objects.nonNull(timeSlotModelList) && timeSlotModelList.size() > NumberConstant.ZERO) {
            List<TimeSlotCollection> timeSlotMongoCollectionList = timeSlotModelList.stream().map(timeSlotModel -> {
                TimeSlotCollection entity = modelMapper.map(timeSlotModel, TimeSlotCollection.class);
                return entity;
            }).collect(Collectors.toList());
            timeSlotRepo.saveAll(timeSlotMongoCollectionList);
        }
        return timeSlotModelList;
    }

    @Override
    public TimeSlotModel getRecordById(Long id) {
        TimeSlotCollection timeSlotMongoCollection = timeSlotRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotMongoCollection, TimeSlotModel.class);
        return timeSlotModel;
    }

    @Override
    public TimeSlotModel updateRecord(Long id, TimeSlotModel record) {
        TimeSlotCollection timeSlotMongoCollection = timeSlotRepo.findById(id).orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, timeSlotMongoCollection);
        timeSlotRepo.save(timeSlotMongoCollection);
        return record;
    }

    @Override
    public void deleteRecordById(Long id) {
        timeSlotRepo.deleteById(id);
    }

    private Comparator<TimeSlotCollection> findSuitableComparator(String sortBy) {
        Comparator<TimeSlotCollection> comparator;
        switch (sortBy) {
            case "startTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getStartTime()
                        .compareTo(timeSlotTwo.getStartTime());
                break;
            }
            case "endTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getEndTime()
                        .compareTo(timeSlotTwo.getEndTime());
                break;
            }
            case "trainerId": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getTrainerId()
                        .compareTo(timeSlotTwo.getTrainerId());
                break;
            }
            default: {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getId()
                        .compareTo(timeSlotTwo.getId());
            }
        }
        return comparator;
    }

}
