package com.learning.service;

import com.learning.entity.StudentEntity;
import com.learning.entity.collections.StudentCollection;
import com.learning.enums.ErrorMessages;
import com.learning.enums.InfoMessages;
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
import java.util.concurrent.CompletableFuture;
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
                .stream().map(studentCollection -> modelMapper.map(studentCollection, StudentModel.class))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(studentModelListMongo)) {
            return studentModelListMongo;
        }
        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy)))
                .stream().map(studentEntity -> modelMapper.map(studentEntity, StudentModel.class))
                .collect(Collectors.toList());
    }

    public List<StudentModel> saveRecords(List<StudentModel> studentModelList) {
        if (!CollectionUtils.isEmpty(studentModelList)) {
            List<StudentEntity> studentEntityList = studentModelList.stream()
                    .map(studentModel -> modelMapper.map(studentModel, StudentEntity.class))
                    .collect(Collectors.toList());
            log.info(InfoMessages.SAVING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.saveAll(studentEntityList);

            CompletableFuture.runAsync(() -> {
                List<StudentCollection> studentCollectionList = studentEntityList.stream()
                        .map(studentEntity -> modelMapper.map(studentEntity, StudentCollection.class))
                        .collect(Collectors.toList());
                log.info(InfoMessages.SAVING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.saveAll(studentCollectionList);
            });
        }
        return studentModelList;
    }


    public StudentModel getRecordById(Long id) {
            if (mongoRepo.existsById(id)) {
            StudentCollection studentCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(studentCollection, StudentModel.class);
        }

        StudentEntity studentEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(studentEntity, StudentModel.class);
    }


    public StudentModel updateRecord(Long id, StudentModel record) {
        StudentEntity studentEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, studentEntity);
        log.info(InfoMessages.UPDATING_DATA_IN_JPA.getInfoMessage());
        jpaRepo.save(studentEntity);

        CompletableFuture.runAsync(() -> {
            StudentCollection studentCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, studentCollection);
            log.info(InfoMessages.UPDATING_DATA_IN_MONGO.getInfoMessage());
            mongoRepo.save(studentCollection);
        });
        return record;
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            log.info(InfoMessages.DELETING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.deleteById(id);

            CompletableFuture.runAsync(() -> {
                log.info(InfoMessages.DELETING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.deleteById(id);
            });
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<StudentEntity> studentEntityList = studentReader.getStudentObjects(file.getInputStream());
                log.info(InfoMessages.SAVING_DATA_IN_JPA.getInfoMessage());
                jpaRepo.saveAll(studentEntityList);

                CompletableFuture.runAsync(() -> {
                    List<StudentCollection> studentCollectionList = studentEntityList.stream()
                            .map(studentEntity -> modelMapper.map(studentEntity, StudentCollection.class))
                            .collect(Collectors.toList());
                    log.info(InfoMessages.SAVING_DATA_IN_MONGO.getInfoMessage());
                    mongoRepo.saveAll(studentCollectionList);
                });
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    public void emailSender() {
        emailSender.mailSenderThread(jpaRepo.findEmails());
    }

    public void sendMailWithAttachment() {
        emailSender.sendMailWithAttachment(jpaRepo.findEmails());
    }

    public void transferMySqlDataToMongo() {
        List<StudentCollection> studentCollection = jpaRepo.findAll().stream()
                .map(studentEntity -> modelMapper.map(studentEntity, StudentCollection.class))
                .collect(Collectors.toList());
        mongoRepo.saveAll(studentCollection);
    }


}

