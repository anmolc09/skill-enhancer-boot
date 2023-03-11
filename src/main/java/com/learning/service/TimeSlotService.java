package com.learning.service;

import com.learning.entity.TimeSlotEntity;
import com.learning.entity.document.TimeSlotDocument;
import com.learning.enums.ErrorMessages;
import com.learning.enums.InfoMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.TimeSlotModel;
import com.learning.repository.mongo.TimeSlotMongoRepository;
import com.learning.repository.mysql.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeSlotService {

    private final TimeSlotRepository jpaRepo;
    private final TimeSlotMongoRepository mongoRepo;
    private final ModelMapper modelMapper;

    public List<TimeSlotModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        List<TimeSlotModel> timeSlotModelListMongo = mongoRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(timeSlotCollection -> modelMapper.map(timeSlotCollection, TimeSlotModel.class))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(timeSlotModelListMongo)) return timeSlotModelListMongo;

        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(timeSlotEntity -> modelMapper.map(timeSlotEntity, TimeSlotModel.class))
                .collect(Collectors.toList());
    }

    public List<TimeSlotModel> saveRecords(List<TimeSlotModel> timeSlotModelList) {
        if (!CollectionUtils.isEmpty(timeSlotModelList)) {
            List<TimeSlotEntity> timeSlotEntityList = timeSlotModelList.stream()
                    .map(timeSlotModel -> modelMapper.map(timeSlotModel, TimeSlotEntity.class))
                    .collect(Collectors.toList());
            log.info(InfoMessages.SAVING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.saveAll(timeSlotEntityList);

            CompletableFuture.runAsync(() -> {
                List<TimeSlotDocument> timeSlotDocumentList = timeSlotEntityList.stream()
                        .map(timeSlotEntity -> modelMapper.map(timeSlotEntity, TimeSlotDocument.class)).collect(Collectors.toList());
                log.info(InfoMessages.SAVING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.saveAll(timeSlotDocumentList);
            });
        }
        return timeSlotModelList;
    }

    public TimeSlotModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)) {
            TimeSlotDocument timeSlotDocument = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(timeSlotDocument, TimeSlotModel.class);
        }
        TimeSlotEntity timeSlotEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(timeSlotEntity, TimeSlotModel.class);
    }

    public TimeSlotModel updateRecord(Long id, TimeSlotModel record) {
        TimeSlotEntity timeSlotEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, timeSlotEntity);
        log.info(InfoMessages.UPDATING_DATA_IN_JPA.getInfoMessage());
        jpaRepo.save(timeSlotEntity);

        CompletableFuture.runAsync(() -> {
            TimeSlotDocument timeSlotDocument = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, timeSlotDocument);
            log.info(InfoMessages.UPDATING_DATA_IN_MONGO.getInfoMessage());
            mongoRepo.save(timeSlotDocument);
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
