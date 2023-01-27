package com.learning.service.mongo;

import com.learning.constants.NumberConstant;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.StudentBatchModel;
import com.learning.collections.StudentBatchCollection;
import com.learning.repository.mongo.StudentBatchMongoRepository;
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
public class StudentBatchMongoService implements CommonService<StudentBatchModel, Long> {

    private final StudentBatchMongoRepository studentBatchRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<StudentBatchModel> getAllRecords() {
        List<StudentBatchCollection> studentBatchCollectionList = studentBatchRepo.findAll();
        if (Objects.nonNull(studentBatchCollectionList) && studentBatchCollectionList.size() > NumberConstant.ZERO) {
            return studentBatchCollectionList.stream().map(studentBatchCollection -> {
                StudentBatchModel studentBatchModel = modelMapper.map(studentBatchCollection, StudentBatchModel.class);
                return studentBatchModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentBatchModel> getLimitedRecords(int count) {
        List<StudentBatchCollection> studentBatchCollectionList = studentBatchRepo.findAll();
        if (Objects.nonNull(studentBatchCollectionList) && studentBatchCollectionList.size() > NumberConstant.ZERO) {
            return studentBatchCollectionList.stream().limit(count)
                    .map(studentBatchCollection -> {
                        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchCollection, StudentBatchModel.class);
                        return studentBatchModel;
                    }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentBatchModel> getSortedRecords(String sortBy) {
        List<StudentBatchCollection> studentBatchCollectionList = studentBatchRepo.findAll();
        if (Objects.nonNull(studentBatchCollectionList) && studentBatchCollectionList.size() > NumberConstant.ZERO) {
            Comparator<StudentBatchCollection> comparator = findSuitableComparator(sortBy);
            return studentBatchCollectionList.stream().sorted(comparator)
                    .map(studentBatchCollection -> {
                        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchCollection, StudentBatchModel.class);
                        return studentBatchModel;
                    }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public StudentBatchModel saveRecord(StudentBatchModel studentBatchModel) {
        if (Objects.nonNull(studentBatchModel)) {
            studentBatchRepo.save(modelMapper.map(studentBatchModel, StudentBatchCollection.class));
        }
        return studentBatchModel;
    }

    @Override
    public List<StudentBatchModel> saveAll(List<StudentBatchModel> studentBatchModelList) {
        if (Objects.nonNull(studentBatchModelList) && studentBatchModelList.size() > NumberConstant.ZERO) {
            List<StudentBatchCollection> studentBatchCollectionList = studentBatchModelList.stream().map(studentBatchModel -> {
                StudentBatchCollection entity = modelMapper.map(studentBatchModel, StudentBatchCollection.class);
                return entity;
            }).collect(Collectors.toList());
            studentBatchRepo.saveAll(studentBatchCollectionList);
        }
        return studentBatchModelList;
    }

    @Override
    public StudentBatchModel getRecordById(Long id) {
        StudentBatchCollection studentBatchCollection = studentBatchRepo.findById(id)
				.orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchCollection, StudentBatchModel.class);
        return studentBatchModel;
    }

    @Override
    public StudentBatchModel updateRecord(Long id, StudentBatchModel record) {
        StudentBatchCollection studentBatchCollection = studentBatchRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, studentBatchCollection);
            studentBatchRepo.save(studentBatchCollection);
            return record;
        }

    @Override
    public void deleteRecordById(Long id) {
        studentBatchRepo.deleteById(id);
    }

    private Comparator<StudentBatchCollection> findSuitableComparator(String sortBy) {
        Comparator<StudentBatchCollection> comparator;
        switch (sortBy) {
            case "fees": {
                comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getFees()
                        .compareTo(studentBatchTwo.getFees());
                break;
            }
            case "batchId": {
                comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getBatchId()
                        .compareTo(studentBatchTwo.getBatchId());
                break;
            }
            case "studentId": {
                comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getStudentId()
                        .compareTo(studentBatchTwo.getStudentId());
                break;
            }
            default: {
                comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getId()
                        .compareTo(studentBatchTwo.getId());
            }
        }
        return comparator;
    }

}
