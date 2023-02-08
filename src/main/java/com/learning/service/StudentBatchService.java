package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.StudentBatchEntity;
import com.learning.enums.ErrorMessages;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

        if(!CollectionUtils.isEmpty(studentBatchModelListMongo)) return studentBatchModelListMongo;

        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(studentBatchEntity -> modelMapper.map(studentBatchEntity, StudentBatchModel.class))
                .collect(Collectors.toList());
    }

    public List<StudentBatchModel> saveRecords(List<StudentBatchModel> studentBatchModelList) {
        if (Objects.nonNull(studentBatchModelList) && studentBatchModelList.size() > NumberConstant.ZERO) {
            List<StudentBatchEntity> studentBatchEntityList = studentBatchModelList.stream().map(studentBatchModel -> {
                StudentBatchEntity entity = modelMapper.map(studentBatchModel, StudentBatchEntity.class);
                return entity;
            }).collect(Collectors.toList());
            jpaRepo.saveAll(studentBatchEntityList);
        }
        return studentBatchModelList;
    }

    public StudentBatchModel getRecordById(Long id) {
        StudentBatchEntity studentBatchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchEntity, StudentBatchModel.class);
        return studentBatchModel;
    }

    public StudentBatchModel updateRecord(Long id, StudentBatchModel record) {
        StudentBatchEntity studentBatchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, studentBatchEntity);
        jpaRepo.save(studentBatchEntity);
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
