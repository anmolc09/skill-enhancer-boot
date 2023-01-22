package com.learning.utility.excel.reader;

import com.learning.entity.StudentEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StudentReader {

    public List<StudentEntity> getStudentObjects(InputStream file) {
        List<StudentEntity> studentEntityList = new ArrayList<>();
        try {
            //FileInputStream file = new FileInputStream(new File(".\\resources\\student-data.xlsx"));

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(1);

            getStudentList(sheet, studentEntityList);

            file.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return studentEntityList;
    }

    private static void getStudentList(XSSFSheet sheet, List<StudentEntity> studentEntityList) {
        for (int index = sheet.getFirstRowNum() + 1; index <= sheet.getLastRowNum(); index++) {
            Row row = sheet.getRow(index);

            StudentEntity studentEntity = new StudentEntity();

            for (int index2 = row.getFirstCellNum(); index2 < row.getLastCellNum(); index2++) {
                Cell cell = row.getCell(index2);
                if (index2 == 0) {
                    studentEntity.setId((long) cell.getNumericCellValue());
                } else if (index2 == 1) {
                    studentEntity.setName(cell.getStringCellValue());
                } else if (index2 == 2) {
                    studentEntity.setContactDetails((long) cell.getNumericCellValue());
                } else if (index2 == 3) {
                    studentEntity.setQualification(cell.getStringCellValue());
                } else if (index2 == 4) {
                    studentEntity.setEmail(cell.getStringCellValue());
                } else {
                    log.error("data not found ");
                }
            }
            studentEntityList.add(studentEntity);
        }
    }
}


