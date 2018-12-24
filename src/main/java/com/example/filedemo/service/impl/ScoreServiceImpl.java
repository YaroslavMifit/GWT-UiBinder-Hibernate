package com.example.filedemo.service.impl;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.Score;
import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.repository.ScoreRepository;
import com.example.filedemo.service.ScoreService;
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
public class ScoreServiceImpl implements ScoreService {
    private static final Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);
    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    @Transactional
    public void saveFileInDataBase(String filePath, CheckBoxResponse checkBoxResponse) {
        logger.info("Начали запись файла NAMES.xlsx в базу");
        try (FileInputStream excelFile = new FileInputStream(new File(filePath));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
            //scoreRepository.deleteAll();
            Set<Score> scoreSet = new HashSet<>();

            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                Score score = new Score();

                XSSFRow row = worksheet.getRow(i);

                score.setId(Long.parseLong(row.getCell(0).getStringCellValue()))
                        .setScoreName(row.getCell(1).getStringCellValue());
                scoreSet.add(score);
            }
            checkBoxResponse.setScore(scoreSet);
            scoreSet.parallelStream().forEach(x -> scoreRepository.save(x));
            logger.info("Успешно записали файл: NAMES.xlsx в базу");
        } catch (IOException ex) {
            throw new FileStorageException("Failed to save file to database: NAMES.xlsx. Please try again!", ex);
        }
    }
}
