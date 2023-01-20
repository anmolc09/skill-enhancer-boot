package com.learning.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstant;
import com.learning.entity.CourseEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.CourseModel;
import com.learning.repository.CourseRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService implements CommonService<CourseModel, Long> {

    private final CourseRepository courseRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<CourseModel> getAllRecords() {
        List<CourseEntity> courseEntityList = courseRepo.findAll();
        if (Objects.nonNull(courseEntityList) && courseEntityList.size() > NumberConstant.ZERO) {
            return courseEntityList.stream().map(courseEntity -> {
                        CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseModel> getLimitedRecords(int count) {
        List<CourseEntity> courseEntityList = courseRepo.findAll();
        if (Objects.nonNull(courseEntityList) && courseEntityList.size() > NumberConstant.ZERO) {
            return courseEntityList.stream().limit(count).map(courseEntity -> {
                        CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseModel> getSortedRecords(String sortBy) {
        List<CourseEntity> courseEntityList = courseRepo.findAll();
        if (Objects.nonNull(courseEntityList) && courseEntityList.size() > NumberConstant.ZERO) {
            Comparator<CourseEntity> comparator = findSuitableComparator(sortBy);
            return courseEntityList.stream().sorted(comparator).map(courseEntity -> {
                        CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public CourseModel saveRecord(CourseModel courseModel) {
        if (Objects.nonNull(courseModel)) {
            courseRepo.save(modelMapper.map(courseModel, CourseEntity.class));
        }
        return courseModel;
    }

    @Override
    public List<CourseModel> saveAll(List<CourseModel> courseModelList) {
        if (Objects.nonNull(courseModelList) && courseModelList.size() > NumberConstant.ZERO) {
            List<CourseEntity> courseEntityList = courseModelList.stream()
                    .map(courseModel -> {
                        CourseEntity entity = modelMapper.map(courseModel, CourseEntity.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            courseRepo.saveAll(courseEntityList);
        }
        return courseModelList;
    }

    @Override
    public CourseModel getRecordById(Long id) {
        CourseEntity courseEntity = courseRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
        return courseModel;
    }

    @Override
    public CourseModel updateRecord(Long id, CourseModel record) {
        CourseEntity courseEntity = courseRepo.findById(id).orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, courseEntity);
        courseRepo.save(courseEntity);
        return record;
    }

    @Override
    public void deleteRecordById(Long id) {
        courseRepo.deleteById(id);
    }

    private Comparator<CourseEntity> findSuitableComparator(String sortBy) {
        Comparator<CourseEntity> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (courseOne, courseTwo) ->
                        courseOne.getName().compareTo(courseTwo.getName());
                break;
            }
            case "curriculum": {
                comparator = (courseOne, courseTwo) ->
                        courseOne.getCurriculum().compareTo(courseTwo.getCurriculum());
                break;
            }
            case "duration": {
                comparator = (courseOne, courseTwo) ->
                        courseOne.getDuration().compareTo(courseTwo.getDuration());
                break;
            }
            default: {
                comparator = (courseOne, courseTwo) ->
                        courseOne.getId().compareTo(courseTwo.getId());
            }
        }
        return comparator;
    }

}
