package com.example.filedemo.service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.Score;
import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.repository.ScoreRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    public void saveFileInDataBase(String filePath, CheckBoxResponse checkBoxResponse) {
        try (FileInputStream excelFile = new FileInputStream(new File(filePath));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
            // Check if the file's name contains invalid characters
            if(filePath.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filePath);
            }
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
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + filePath + ". Please try again!", ex);
        }
    }
}
