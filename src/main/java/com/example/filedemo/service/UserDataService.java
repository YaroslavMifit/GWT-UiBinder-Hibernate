package com.example.filedemo.service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.UserData;
import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.repository.CreditOrganizationRepository;
import com.example.filedemo.repository.ScoreRepository;
import com.example.filedemo.repository.UserDataRepository;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.*;
import java.util.*;

@Service
public class UserDataService {
    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CreditOrganizationRepository creditOrganizationRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserData> getListUserDataFilter(){
        Query query;

        if(fileStorageService.getListUserDataFilter(0).size() != 0 && fileStorageService.getListUserDataFilter(1).size() != 0) {
            query = entityManager.createQuery("FROM UserData WHERE creditOrganization.id IN (:creditOrganizationID) AND score.id in (:scoreID) ORDER BY creditOrganization.id");
            query.setParameter("creditOrganizationID", fileStorageService.getListUserDataFilter(0));
            query.setParameter("scoreID", fileStorageService.getListUserDataFilter(1));
        } else if (fileStorageService.getListUserDataFilter(0).size() != 0){
            query = entityManager.createQuery("FROM UserData WHERE creditOrganization.id IN (:creditOrganizationID) ORDER BY creditOrganization.id");
            query.setParameter("creditOrganizationID", fileStorageService.getListUserDataFilter(0));
        } else if (fileStorageService.getListUserDataFilter(1).size() != 0){
            query = entityManager.createQuery("FROM UserData WHERE score.id in (:scoreID) ORDER BY creditOrganization.id");
            query.setParameter("scoreID", fileStorageService.getListUserDataFilter(1));
        } else {
            query = entityManager.createQuery("FROM UserData ORDER BY creditOrganization.id");
        }

        List<UserData> userDataList = query.getResultList();
        return userDataList;
    }

    public void saveFileInDataBase(String filePath, CheckBoxResponse checkBoxResponse) {
        userDataRepository.deleteAll();
        try (InputStream is = new FileInputStream(new File(filePath))) {

            Sheet sheet = StreamingReader.builder().open(is).getSheetAt(0);
            List<UserData> userDataSet = new LinkedList<>();
                System.out.println("Processing sheet: " + sheet.getSheetName());
                for (Row row : sheet) {
                    if (row.getRowNum() == 0){
                        List<String> list = new ArrayList<>();
                        row.forEach(x -> list.add(x.getStringCellValue()));
                        checkBoxResponse.setUserDataTitle(list);
                        continue;
                    }

                    if(!row.getCell(0).getStringCellValue().matches("\\d+")
                            || !row.getCell(1).getStringCellValue().matches("\\d+")) {
                        continue;
                    }

                    UserData userData = new UserData();
                    userData.setCreditOrganization(creditOrganizationRepository.getOne(Long.parseLong(row.getCell(0).getStringCellValue())))
                            .setScore(scoreRepository.getOne(Long.parseLong(row.getCell(1).getStringCellValue())))
                            .setIncomingBalancesInRubles(row.getCell(2).getStringCellValue())
                            .setIncomingBalancesDragMetals(row.getCell(3).getStringCellValue())
                            .setIncomingBalancesOfTotal(row.getCell(4).getStringCellValue())
                            .setTurnoversDebitInRubles(row.getCell(5).getStringCellValue())
                            .setTurnoversDebitDragMetals(row.getCell(6).getStringCellValue())
                            .setTurnoversDebitOfTotal(row.getCell(7).getStringCellValue())
                            .setTurnoversCreditInRubles(row.getCell(8).getStringCellValue())
                            .setTurnoversCreditDragMetals(row.getCell(9).getStringCellValue())
                            .setTurnoversCreditOfTotal(row.getCell(10).getStringCellValue())
                            .setOutgoingBalancesInRubles(row.getCell(11).getStringCellValue())
                            .setOutgoingBalancesDragMetals(row.getCell(12).getStringCellValue())
                            .setOutgoingBalancesOfTotal(row.getCell(13).getStringCellValue());

                    userDataSet.add(userData);

                }
            userDataSet.parallelStream().forEach(x -> userDataRepository.save(x));

        }catch (IOException ex) {
            throw new FileStorageException("Could not store file " + filePath + ". Please try again!", ex);
        }
    }
}
