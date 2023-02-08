package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.CourseEntity;
import com.learning.enums.ErrorMessages;
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
import java.util.Objects;
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
        if (Objects.nonNull(courseModelList) && courseModelList.size() > NumberConstant.ZERO) {
            List<CourseEntity> courseEntityList = courseModelList.stream()
                    .map(courseModel -> {
                        CourseEntity entity = modelMapper.map(courseModel, CourseEntity.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            jpaRepo.saveAll(courseEntityList);
        }
        return courseModelList;
    }

    public CourseModel getRecordById(Long id) {
        CourseEntity courseEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
        return courseModel;
    }

    public CourseModel updateRecord(Long id, CourseModel record) {
        CourseEntity courseEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, courseEntity);
        jpaRepo.save(courseEntity);
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
