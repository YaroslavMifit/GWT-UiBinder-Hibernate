package com.example.filedemo.service.impl;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.CreditOrganization;
import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.repository.CreditOrganizationRepository;
import com.example.filedemo.service.CreditOrganizationService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class CreditOrganizationServiceImpl implements CreditOrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(CreditOrganizationServiceImpl.class);

    @Autowired
    private CreditOrganizationRepository creditOrganizationRepository;

    @Override
    @Transactional
    public void saveFileInDataBase(String filePath, CheckBoxResponse checkBoxResponse) {
        logger.info("Начали запись файла 092018N1.xlsx в базу");
        try (FileInputStream excelFile = new FileInputStream(new File(filePath));
              XSSFWorkbook workbook = new XSSFWorkbook(excelFile)){

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
            logger.info("Успешно записали файл: 092018N1.xlsx в базу");
        } catch (IOException ex) {
            throw new FileStorageException("Failed to save file to database: 092018N1.xlsx. Please try again!", ex);
        }
    }
}
