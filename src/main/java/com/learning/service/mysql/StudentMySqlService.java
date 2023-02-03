package com.learning.service.mysql;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstant;
import com.learning.entity.StudentEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.StudentModel;
import com.learning.repository.mysql.StudentRepository;
import com.learning.utility.excel.reader.StudentReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentMySqlService {

    private final StudentRepository studentRepo;
    private final ModelMapper modelMapper;
    private final StudentReader studentReader;

    public List<StudentModel> getAllRecords(int count, String sortBy) {
        List<StudentEntity> studentEntityList = studentRepo.findAll();
        Comparator<StudentEntity> comparator = findSuitableComparator(sortBy);
        List<StudentModel> studentModelList = new ArrayList<>();
        if (Objects.nonNull(studentEntityList) && studentEntityList.size() > NumberConstant.ZERO) {
            if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
                studentModelList = studentEntityList.stream().map(studentEntity -> {
                    StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                    return studentModel;
                }).collect(Collectors.toList());
            } else if (count > 0) {
                studentModelList = studentEntityList.stream().limit(count).map(studentEntity -> {
                    StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                    return studentModel;
                }).collect(Collectors.toList());
            } else {
                studentModelList = studentEntityList.stream().sorted(comparator).map(studentEntity -> {
                    StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                    return studentModel;
                }).collect(Collectors.toList());
            }
            return studentModelList;
        }
        return Collections.emptyList();
    }

    public List<StudentModel> saveRecords(List<StudentModel> studentModelList) {
        if (Objects.nonNull(studentModelList) && studentModelList.size() > NumberConstant.ZERO) {
            if (studentModelList.size() == 1) {
                StudentEntity entity = modelMapper.map(studentModelList.get(0), StudentEntity.class);
                studentRepo.save(entity);
                return Arrays.asList(studentModelList.get(0));
            }
            List<StudentEntity> studentEntityList = studentModelList.stream().map(studentModel -> {
                StudentEntity entity = modelMapper.map(studentModel, StudentEntity.class);
                return entity;
            }).collect(Collectors.toList());
            studentRepo.saveAll(studentEntityList);
        }
        return studentModelList;
    }

    public StudentModel getRecordById(Long id) {
        StudentEntity studentEntity = studentRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
        return studentModel;
    }

    public StudentModel updateRecord(Long id, StudentModel record) {
        StudentEntity studentEntity = studentRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, studentEntity);
            studentRepo.save(studentEntity);
            return record;
        }

    public void deleteRecordById(Long id) {
        if (studentRepo.existsById(id)) {
            studentRepo.deleteById(id);
            log.info("deleted");
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
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
