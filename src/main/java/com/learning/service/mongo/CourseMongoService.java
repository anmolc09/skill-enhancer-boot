package com.learning.service.mongo;

import com.learning.constants.NumberConstant;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.CourseModel;
import com.learning.entity.collections.CourseCollection;
import com.learning.repository.mongo.CourseMongoRepository;
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
public class CourseMongoService implements CommonService<CourseModel, Long> {

    private final CourseMongoRepository courseRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<CourseModel> getAllRecords() {
        List<CourseCollection> courseCollectionList = courseRepo.findAll();
        if (Objects.nonNull(courseCollectionList) && courseCollectionList.size() > NumberConstant.ZERO) {
            return courseCollectionList.stream().map(courseCollection -> {
                        CourseModel courseModel = modelMapper.map(courseCollection, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseModel> getLimitedRecords(int count) {
        List<CourseCollection> courseCollectionList = courseRepo.findAll();
        if (Objects.nonNull(courseCollectionList) && courseCollectionList.size() > NumberConstant.ZERO) {
            return courseCollectionList.stream().limit(count).map(courseCollection -> {
                        CourseModel courseModel = modelMapper.map(courseCollection, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseModel> getSortedRecords(String sortBy) {
        List<CourseCollection> courseCollectionList = courseRepo.findAll();
        if (Objects.nonNull(courseCollectionList) && courseCollectionList.size() > NumberConstant.ZERO) {
            Comparator<CourseCollection> comparator = findSuitableComparator(sortBy);
            return courseCollectionList.stream().sorted(comparator).map(courseCollection -> {
                        CourseModel courseModel = modelMapper.map(courseCollection, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public CourseModel saveRecord(CourseModel courseModel) { //object
        if (Objects.nonNull(courseModel)) {
            courseRepo.save(modelMapper.map(courseModel, CourseCollection.class));
        }
        return courseModel;
    }

    @Override
    public List<CourseModel> saveAll(List<CourseModel> courseModelList) {
        if (Objects.nonNull(courseModelList) && courseModelList.size() > NumberConstant.ZERO) {
            List<CourseCollection> courseCollectionList = courseModelList.stream()
                    .map(courseModel -> {
                        CourseCollection entity = modelMapper.map(courseModel, CourseCollection.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            courseRepo.saveAll(courseCollectionList);
        }
        return courseModelList;
    }

    @Override
    public CourseModel getRecordById(Long id) {
        CourseCollection courseCollection = courseRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        CourseModel courseModel = modelMapper.map(courseCollection, CourseModel.class);
        return courseModel;
    }

    @Override
    public CourseModel updateRecord(Long id, CourseModel record) {
        CourseCollection courseCollection = courseRepo.findById(id).orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, courseCollection);
        courseRepo.save(courseCollection);
        return record;
    }

    @Override
    public void deleteRecordById(Long id) {
        courseRepo.deleteById(id);
    }

    private Comparator<CourseCollection> findSuitableComparator(String sortBy) {
        Comparator<CourseCollection> comparator;
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
