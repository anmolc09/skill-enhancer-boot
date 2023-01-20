package com.learning.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstant;
import com.learning.entity.StudentEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.StudentModel;
import com.learning.repository.StudentRepository;
import com.learning.service.CommonService;
import com.learning.utility.excel.reader.StudentReader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StudentService implements CommonService<StudentModel, Long> {

    private final StudentRepository studentRepo;
    private final ModelMapper modelMapper;
    private final StudentReader studentReader;

    @Override
    public List<StudentModel> getAllRecords() {
        List<StudentEntity> studentEntityList = studentRepo.findAll();
        if (Objects.nonNull(studentEntityList) && studentEntityList.size() > NumberConstant.ZERO) {
           return studentEntityList.stream().map(studentEntity -> {
                StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                return studentModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentModel> getLimitedRecords(int count) {
        List<StudentEntity> studentEntityList = studentRepo.findAll();
        if (Objects.nonNull(studentEntityList) && studentEntityList.size() > NumberConstant.ZERO) {
            return studentEntityList.stream().limit(count).map(studentEntity -> {
                StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                return studentModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentModel> getSortedRecords(String sortBy) {
        List<StudentEntity> studentEntityList = studentRepo.findAll();
        if (Objects.nonNull(studentEntityList) && studentEntityList.size() > NumberConstant.ZERO) {
            Comparator<StudentEntity> comparator = findSuitableComparator(sortBy);
           return studentEntityList.stream().sorted(comparator).map(studentEntity -> {
                StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                return studentModel;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public StudentModel saveRecord(StudentModel studentModel) {
        if (Objects.nonNull(studentModel)) {
            StudentEntity entity = modelMapper.map(studentModel, StudentEntity.class);
            studentRepo.save(entity);
        }
        return studentModel;
    }

    @Override
    public List<StudentModel> saveAll(List<StudentModel> studentModelList) {
        if (Objects.nonNull(studentModelList) && studentModelList.size() > NumberConstant.ZERO) {
            List<StudentEntity> studentEntityList = studentModelList.stream().map(studentModel -> {
                StudentEntity entity = modelMapper.map(studentModel, StudentEntity.class);
                return entity;
            }).collect(Collectors.toList());
            studentRepo.saveAll(studentEntityList);
        }
        return studentModelList;
    }

    @Override
    public StudentModel getRecordById(Long id) {
        StudentEntity studentEntity = studentRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
        return studentModel;
    }

    @Override
    public StudentModel updateRecord(Long id, StudentModel record) {
        StudentEntity studentEntity = studentRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, studentEntity);
            studentRepo.save(studentEntity);
            return record;
        }

    @Override
    public void deleteRecordById(Long id) {
        studentRepo.deleteById(id);
    }

    public void saveExcelFile(MultipartFile file) {
        //check that file is of excel type or not
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<StudentEntity> studentEntityList = studentReader.getStudentObjects(file.getInputStream());
                studentRepo.saveAll(studentEntityList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

       private Comparator<StudentEntity> findSuitableComparator(String sortBy) {
        Comparator<StudentEntity> comparator;
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
