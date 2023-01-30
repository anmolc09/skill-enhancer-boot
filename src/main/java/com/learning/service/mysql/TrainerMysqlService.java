package com.learning.service.mysql;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstant;
import com.learning.entity.TrainerEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.TrainerModel;
import com.learning.repository.mysql.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerMysqlService {

    private final TrainerRepository trainerRepo;
    private final ModelMapper modelMapper;

    public List<TrainerModel> getAllRecords() {
        List<TrainerEntity> trainerEntityList = trainerRepo.findAll();
        if (Objects.nonNull(trainerEntityList) && trainerEntityList.size() > NumberConstant.ZERO) {
            return trainerEntityList.stream()
                    .map(trainerEntity -> {
                        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
                        return trainerModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<TrainerModel> getLimitedRecords(int count) {
        List<TrainerEntity> trainerEntityList = trainerRepo.findAll();
        if (Objects.nonNull(trainerEntityList) && trainerEntityList.size() > NumberConstant.ZERO) {
            return trainerEntityList.stream()
                    .limit(count)
                    .map(trainerEntity -> {
                        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
                        return trainerModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<TrainerModel> getSortedRecords(String sortBy) {
        List<TrainerEntity> trainerEntityList = trainerRepo.findAll();
        if (Objects.nonNull(trainerEntityList) && trainerEntityList.size() > NumberConstant.ZERO) {
            Comparator<TrainerEntity> comparator = findSuitableComparator(sortBy);
            return trainerEntityList.stream()
                    .sorted(comparator)
                    .map(trainerEntity -> {
                        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
                        return trainerModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public TrainerModel saveRecord(TrainerModel trainerModel) {
        if (Objects.nonNull(trainerModel)) {
            TrainerEntity entity = modelMapper.map(trainerModel, TrainerEntity.class);
            trainerRepo.save(entity);
        }
        return trainerModel;
    }

    public List<TrainerModel> saveAll(List<TrainerModel> trainerModelList) {
        if (Objects.nonNull(trainerModelList) && trainerModelList.size() > NumberConstant.ZERO) {
            List<TrainerEntity> trainerEntityList = trainerModelList.stream()
                    .map(trainerModel -> {
                        TrainerEntity entity = modelMapper.map(trainerModel, TrainerEntity.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            trainerRepo.saveAll(trainerEntityList);
        }
        return trainerModelList;
    }

    public TrainerModel getRecordById(Long id) {
        TrainerEntity trainerEntity = trainerRepo.findById(id)
				.orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
        return trainerModel;
    }

    public TrainerModel updateRecord(Long id, TrainerModel record) {
        TrainerEntity trainerEntity = trainerRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record,trainerEntity);
        trainerRepo.save(trainerEntity);
        return  record;
    }

    public void deleteRecordById(Long id) {
        trainerRepo.deleteById(id);
    }

    private Comparator<TrainerEntity> findSuitableComparator(String sortBy) {
        Comparator<TrainerEntity> comparator;
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
