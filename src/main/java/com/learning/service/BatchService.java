package com.learning.service;

import com.learning.entity.BatchEntity;
import com.learning.entity.document.BatchDocument;
import com.learning.enums.ErrorMessages;
import com.learning.enums.InfoMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.models.BatchModel;
import com.learning.repository.mongo.BatchMongoRepository;
import com.learning.repository.mysql.BatchRepository;
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
public class BatchService {

    private final BatchRepository jpaRepo;
    private final BatchMongoRepository mongoRepo;
    private final ModelMapper modelMapper;


    public List<BatchModel> getAllRecordByPaginationAndSorting(int page, int limit, String sortBy) {
        List<BatchModel> batchModelListMongo = mongoRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy))).stream()
                .map(batchCollection -> modelMapper.map(batchCollection, BatchModel.class)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(batchModelListMongo)) {
            return batchModelListMongo;
        }
        return jpaRepo.findAll(PageRequest.of(page, limit, Sort.by(sortBy)))
                .stream().map(batchEntity -> modelMapper.map(batchEntity, BatchModel.class))
                .collect(Collectors.toList());
    }

    public List<BatchModel> saveRecords(List<BatchModel> batchModelList) {
        if (!CollectionUtils.isEmpty(batchModelList)) {
            List<BatchEntity> batchEntityList = batchModelList.stream()
                    .map(batchModel -> modelMapper.map(batchModel, BatchEntity.class))
                    .collect(Collectors.toList());
            log.info(InfoMessages.SAVING_DATA_IN_JPA.getInfoMessage());
            jpaRepo.saveAll(batchEntityList);

            CompletableFuture.runAsync(() -> {
                List<BatchDocument> batchCollectionList = batchEntityList
                        .stream().map(batchEntity -> modelMapper.map(batchEntity, BatchDocument.class))
                        .collect(Collectors.toList());
                log.info(InfoMessages.SAVING_DATA_IN_MONGO.getInfoMessage());
                mongoRepo.saveAll(batchCollectionList);
            });
        }
        return batchModelList;
    }

    public BatchModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)) {
            BatchDocument batchCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(batchCollection, BatchModel.class);
        }
        BatchEntity batchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(batchEntity, BatchModel.class);
    }

    public BatchModel updateRecord(Long id, BatchModel record) {
        BatchEntity batchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, batchEntity);
        log.info(InfoMessages.UPDATING_DATA_IN_JPA.getInfoMessage());
        jpaRepo.save(batchEntity);

        CompletableFuture.runAsync(() -> {
            BatchDocument batchCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, batchCollection);
            log.info(InfoMessages.UPDATING_DATA_IN_MONGO.getInfoMessage());
            mongoRepo.save(batchCollection);
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
