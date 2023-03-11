package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.StudentBatchEntity;
import com.learning.entity.document.StudentBatchDocument;
import com.learning.enums.ErrorMessages;
import com.learning.enums.InfoMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.StudentBatchModel;
import com.learning.repository.mongo.StudentBatchMongoRepository;
import com.learning.repository.mysql.StudentBatchRepository;
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
public class StudentBatchService {

    private final StudentBatchRepository jpaRepo;
    private final StudentBatchMongoRepository mongoRepo;
    private final ModelMapper modelMapper;

    public List<StudentBatchModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        List<StudentBatchModel> studentBatchModelListMongo = mongoRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(studentBatchCollection -> modelMapper.map(studentBatchCollection, StudentBatchModel.class))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(studentBatchModelListMongo)) return studentBatchModelListMongo;

        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(studentBatchEntity -> modelMapper.map(studentBatchEntity, StudentBatchModel.class))
                .collect(Collectors.toList());
    }

    public List<StudentBatchModel> saveRecords(List<StudentBatchModel> studentBatchModelList) {
        if (!CollectionUtils.isEmpty(studentBatchModelList)) {
            List<StudentBatchEntity> studentBatchEntityList = studentBatchModelList.stream()
                    .map(studentBatchModel -> modelMapper.map(studentBatchModel, StudentBatchEntity.class))
                    .collect(Collectors.toList());
            log.info(InfoMessages.SAVING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.saveAll(studentBatchEntityList);

            CompletableFuture.runAsync(() ->
            {
                List<StudentBatchDocument> studentBatchDocumentList = studentBatchEntityList.stream()
                        .map(studentBatchEntity -> modelMapper.map(studentBatchEntity, StudentBatchDocument.class))
                        .collect(Collectors.toList());
                log.info(InfoMessages.SAVING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.saveAll(studentBatchDocumentList);
            });
        }
        return studentBatchModelList;
    }

    public StudentBatchModel getRecordById(Long id) {
        if(mongoRepo.existsById(id)){
            StudentBatchDocument studentBatchDocument = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException((ErrorMessages.NO_RECORD_FOUND.getErrorMessage())));
            return modelMapper.map(studentBatchDocument, StudentBatchModel.class);
        }

        StudentBatchEntity studentBatchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(studentBatchEntity, StudentBatchModel.class);
    }

    public StudentBatchModel updateRecord(Long id, StudentBatchModel record) {
        StudentBatchEntity studentBatchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, studentBatchEntity);
        log.info(InfoMessages.UPDATING_DATA_IN_JPA.getInfoMessage());
        jpaRepo.save(studentBatchEntity);

        CompletableFuture.runAsync(() -> {
            StudentBatchDocument studentBatchDocument = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record,studentBatchDocument);
            log.info(InfoMessages.UPDATING_DATA_IN_MONGO.getInfoMessage());
        mongoRepo.save(studentBatchDocument);
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
