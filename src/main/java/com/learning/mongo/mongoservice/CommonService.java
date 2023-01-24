package com.learning.mongo.mongoservice;

import java.util.List;

public interface CommonService<T, ID> {
	
	List<T> getAllRecords();
	
	List<T> getLimitedRecords(int count);
	
	List<T> getSortedRecords(String sortBy);
	
	T saveRecord(T record);
	
	List<T> saveAll(List<T> recordList);

	T getRecordById(ID id);

	T updateRecord(ID id ,T record);
    
    void deleteRecordById(ID id);

}
