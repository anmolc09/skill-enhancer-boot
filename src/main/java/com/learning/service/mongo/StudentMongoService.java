package com.learning.service.mongo;

import com.learning.constants.NumberConstant;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.StudentModel;
import com.learning.collections.StudentCollection;
import com.learning.repository.mongo.StudentMongoRepository;
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
public class StudentMongoService implements CommonService<StudentModel, Long> {

    private final StudentMongoRepository studentRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<StudentModel> getAllRecords() {
        List<StudentCollection> studentCollectionList = studentRepo.findAll();
        if (Objects.nonNull(studentCollectionList) && studentCollectionList.size() > NumberConstant.ZERO) {
           return studentCollectionList.stream().map(studentCollection -> {
                StudentModel studentModel = modelMapper.map(studentCollection, StudentModel.class);
                return studentModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentModel> getLimitedRecords(int count) {
        List<StudentCollection> studentCollectionList = studentRepo.findAll();
        if (Objects.nonNull(studentCollectionList) && studentCollectionList.size() > NumberConstant.ZERO) {
            return studentCollectionList.stream().limit(count).map(studentCollection -> {
                StudentModel studentModel = modelMapper.map(studentCollection, StudentModel.class);
                return studentModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentModel> getSortedRecords(String sortBy) {
        List<StudentCollection> studentCollectionList = studentRepo.findAll();
        if (Objects.nonNull(studentCollectionList) && studentCollectionList.size() > NumberConstant.ZERO) {
            Comparator<StudentCollection> comparator = findSuitableComparator(sortBy);
           return studentCollectionList.stream().sorted(comparator).map(studentCollection -> {
                StudentModel studentModel = modelMapper.map(studentCollection, StudentModel.class);
                return studentModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public StudentModel saveRecord(StudentModel studentModel) {
        if (Objects.nonNull(studentModel)) {
            StudentCollection entity = modelMapper.map(studentModel, StudentCollection.class);
            studentRepo.save(entity);
        }
        return studentModel;
    }

    @Override
    public List<StudentModel> saveAll(List<StudentModel> studentModelList) {
        if (Objects.nonNull(studentModelList) && studentModelList.size() > NumberConstant.ZERO) {
            List<StudentCollection> studentCollectionList = studentModelList.stream().map(studentModel -> {
                StudentCollection entity = modelMapper.map(studentModel, StudentCollection.class);
                return entity;
            }).collect(Collectors.toList());
            studentRepo.saveAll(studentCollectionList);
        }
        return studentModelList;
    }

    @Override
    public StudentModel getRecordById(Long id) {
        StudentCollection studentCollection = studentRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        StudentModel studentModel = modelMapper.map(studentCollection, StudentModel.class);
        return studentModel;
    }

    @Override
    public StudentModel updateRecord(Long id, StudentModel record) {
        StudentCollection studentCollection = studentRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, studentCollection);
            studentRepo.save(studentCollection);
            return record;
        }

    @Override
    public void deleteRecordById(Long id) {
        studentRepo.deleteById(id);
    }

  /*  public void saveExcelFile(MultipartFile file) {
        //check that file is of excel type or not
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<StudentCollection> studentCollectionList = studentReader.getStudentObjects(file.getInputStream());
                studentRepo.saveAll(studentCollectionList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
       private Comparator<StudentCollection> findSuitableComparator(String sortBy) {
        Comparator<StudentCollection> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (studentOne, studentTwo) -> studentOne.getName().compareTo(studentTwo.getName());
                break;
            }
            case "email": {
                comparator = (studentOne, studentTwo) -> studentOne.getEmail().compareTo(studentTwo.getEmail());
                break;
            }
            default: {
                comparator = (studentOne, studentTwo) -> studentOne.getId().compareTo(studentTwo.getId());
            }
        }
        return comparator;
    }

}
