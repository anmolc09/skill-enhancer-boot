package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.TrainerEntity;
import com.learning.entity.document.TrainerDocument;
import com.learning.enums.ErrorMessages;
import com.learning.enums.InfoMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.TrainerModel;
import com.learning.repository.mongo.TrainerMongoRepository;
import com.learning.repository.mysql.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {

    private final TrainerRepository jpaRepo;
    private final TrainerMongoRepository mongoRepo;
    private final ModelMapper modelMapper;

    public List<TrainerModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        List<TrainerModel> trainerModelListMongo = mongoRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(trainerCollection -> modelMapper.map(trainerCollection, TrainerModel.class))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(trainerModelListMongo)) return trainerModelListMongo;

        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerModel.class))
                .collect(Collectors.toList());
    }

    public List<TrainerModel> saveRecords(List<TrainerModel> trainerModelList) {
        if (!CollectionUtils.isEmpty(trainerModelList)) {
            List<TrainerEntity> trainerEntityList = trainerModelList.stream()
                    .map(trainerModel -> modelMapper.map(trainerModel, TrainerEntity.class))
                    .collect(Collectors.toList());
            log.info(InfoMessages.SAVING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.saveAll(trainerEntityList);

            CompletableFuture.runAsync(() -> {
                List<TrainerDocument> trainerDocumentList = trainerEntityList.stream()
                        .map((trainerEntity -> modelMapper.map(trainerEntity, TrainerDocument.class)))
                        .collect(Collectors.toList());
                log.info(InfoMessages.SAVING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.saveAll(trainerDocumentList);
            });
        }
        return trainerModelList;
    }

    public TrainerModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)) {
            TrainerDocument trainerDocument = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(trainerDocument,TrainerModel.class);
        }
            TrainerEntity trainerEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
        return trainerModel;
    }

    public TrainerModel updateRecord(Long id, TrainerModel record) {
        TrainerEntity trainerEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, trainerEntity);
        log.info(InfoMessages.UPDATING_DATA_IN_JPA.getInfoMessage());
        jpaRepo.save(trainerEntity);

        CompletableFuture.runAsync(() -> {
            TrainerDocument trainerDocument = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, trainerDocument);
            log.info(InfoMessages.UPDATING_DATA_IN_MONGO.getInfoMessage());
            mongoRepo.save(trainerDocument);
        });
        return record;
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            log.info(InfoMessages.DELETING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.deleteById(id);

            CompletableFuture.runAsync(() -> {
                log.info(InfoMessages.DELETING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.deleteById(id);
            });
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }


}
