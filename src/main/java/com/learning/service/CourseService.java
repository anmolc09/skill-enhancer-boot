package com.learning.service;

import com.learning.entity.CourseEntity;
import com.learning.entity.document.CourseDocument;
import com.learning.enums.ErrorMessages;
import com.learning.enums.InfoMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.CourseModel;
import com.learning.repository.mongo.CourseMongoRepository;
import com.learning.repository.mysql.CourseRepository;
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
public class CourseService {

    private final CourseRepository jpaRepo;
    private final CourseMongoRepository mongoRepo;
    private final ModelMapper modelMapper;

    public List<CourseModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        List<CourseModel> courseModelListMongo = mongoRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(courseCollection -> modelMapper.map(courseCollection, CourseModel.class))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(courseModelListMongo)) return courseModelListMongo;

        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(courseEntity -> modelMapper.map(courseEntity, CourseModel.class))
                .collect(Collectors.toList());
    }

    public List<CourseModel> saveRecords(List<CourseModel> courseModelList) {
        if (!CollectionUtils.isEmpty(courseModelList)) {
            List<CourseEntity> courseEntityList = courseModelList.stream()
                    .map(courseModel -> modelMapper.map(courseModel, CourseEntity.class))
                    .collect(Collectors.toList());
            log.info(InfoMessages.SAVING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.saveAll(courseEntityList);

            CompletableFuture.runAsync(() -> {
                List<CourseDocument> courseCollectionList = courseEntityList.stream()
                        .map(courseEntity -> modelMapper.map(courseEntity, CourseDocument.class))
                        .collect(Collectors.toList());
                log.info(InfoMessages.SAVING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.saveAll(courseCollectionList);
            });
        }
        return courseModelList;
    }

    public CourseModel getRecordById(Long id) {
        if(mongoRepo.existsById(id)){
            CourseDocument courseCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(courseCollection,CourseModel.class);
        }

        CourseEntity courseEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(courseEntity, CourseModel.class);
    }

    public CourseModel updateRecord(Long id, CourseModel record) {
        CourseEntity courseEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, courseEntity);
        log.info(InfoMessages.UPDATING_DATA_IN_JPA.getInfoMessage());
        jpaRepo.save(courseEntity);

        CompletableFuture.runAsync(() -> {
            CourseDocument courseCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, courseCollection);
            log.info(InfoMessages.UPDATING_DATA_IN_MONGO.getInfoMessage());
            mongoRepo.save(courseCollection);
        });

        return record;
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            log.info(InfoMessages.DELETING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.deleteById(id);
            log.info(String.format(InfoMessages.DELETED_SUCCESSFULLY.getInfoMessage(), id));

            CompletableFuture.runAsync(() -> {
                log.info(InfoMessages.DELETING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.deleteById(id);
            });
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }
}
