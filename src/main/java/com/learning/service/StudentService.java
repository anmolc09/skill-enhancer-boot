package com.learning.service;

import com.learning.constants.NumberConstant;
import com.learning.entity.StudentEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.StudentModel;
import com.learning.repository.mongo.StudentMongoRepository;
import com.learning.repository.mysql.StudentRepository;
import com.learning.utility.email.EmailSender;
import com.learning.utility.excel.reader.StudentReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository jpaRepo;
    private final StudentMongoRepository mongoRepo;
    private final EmailSender emailSender;
    private final ModelMapper modelMapper;
    private final StudentReader studentReader;

    public List<StudentModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        List<StudentModel> studentModelListMongo = mongoRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy)))
                .stream().map(studentEntity -> modelMapper.map(studentEntity, StudentModel.class))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(studentModelListMongo)) {
            return studentModelListMongo;
        }
        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy)))
                .stream().map(studentEntity -> modelMapper.map(studentEntity, StudentModel.class))
                .collect(Collectors.toList());
    }

    public List<StudentModel> saveRecords(List<StudentModel> studentModelList) {
        if (Objects.nonNull(studentModelList) && studentModelList.size() > NumberConstant.ZERO) {
            List<StudentEntity> studentEntityList = convertModelListToEntityList(studentModelList);
            jpaRepo.saveAll(studentEntityList);
        }
        return studentModelList;
    }


    public StudentModel getRecordById(Long id) {
        StudentEntity studentEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
        return studentModel;
    }

    public StudentModel updateRecord(Long id, StudentModel record) {
        StudentEntity studentEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, studentEntity);
        jpaRepo.save(studentEntity);
        return record;
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            jpaRepo.deleteById(id);
            log.info("deleted");
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<StudentEntity> studentEntityList = studentReader.getStudentObjects(file.getInputStream());
                jpaRepo.saveAll(studentEntityList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void emailSender() {
        List<String> emails = jpaRepo.findEmails();
        emailSender.mailSenderThread(emails);
        ;
    }

    private List<StudentEntity> convertModelListToEntityList(List<StudentModel> studentModelList) {
        List<StudentEntity> studentEntityList = studentModelList.stream().map(studentModel -> {
            StudentEntity entity = modelMapper.map(studentModel, StudentEntity.class);
            return entity;
        }).collect(Collectors.toList());
        return studentEntityList;
    }

}

