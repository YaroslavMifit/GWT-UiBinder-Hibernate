package com.example.filedemo.service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.CreditOrganization;
import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.repository.CreditOrganizationRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class CreditOrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(CreditOrganizationService.class);
    @Autowired
    private CreditOrganizationRepository creditOrganizationRepository;

    public void saveFileInDataBase(String filePath, CheckBoxResponse checkBoxResponse) {
        logger.info("Начали запись файла в базу");
        try (FileInputStream excelFile = new FileInputStream(new File(filePath));
              XSSFWorkbook workbook = new XSSFWorkbook(excelFile)){
            // Check if the file's name contains invalid characters
            if(filePath.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filePath);
            }

            Set<CreditOrganization> creditOrganizationSet = new HashSet<CreditOrganization>();
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                CreditOrganization creditOrganization = new CreditOrganization();

                XSSFRow row = worksheet.getRow(i);

                creditOrganization.setId((long) row.getCell(0).getNumericCellValue())
                                    .setCreditOrganizationName(row.getCell(1).getStringCellValue());
                creditOrganizationSet.add(creditOrganization);
            }
            checkBoxResponse.setCreditOrganization(creditOrganizationSet);
            creditOrganizationSet.parallelStream().forEach(x -> creditOrganizationRepository.save(x));
            logger.info("Успешно записали файл в базу");
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + filePath + ". Please try again!", ex);
        }
    }

}
