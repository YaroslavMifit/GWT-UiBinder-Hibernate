package com.example.filedemo.service.impl;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.property.FileStorageProperties;
import com.example.filedemo.service.CreditOrganizationService;
import com.example.filedemo.service.FileStorageService;
import com.example.filedemo.service.ScoreService;
import com.example.filedemo.service.UserDataService;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final Path fileStorageLocation;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CreditOrganizationService creditOrganizationService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public CheckBoxResponse storeFile(MultipartFile file) {
        logger.info("Начали разархивацию RAR файла");
        cleanDirectory();
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Archive rar = new Archive(targetLocation.toFile());
            FileHeader fh = rar.nextFileHeader();

            while(fh != null){
                if (fh.isDirectory()) {
                    System.out.println(fh.toString());
                    System.out.println("directory: " + fh.getFileNameString() );
                }
                File out = new File(this.fileStorageLocation.resolve(fh.getFileNameString()).toString());
                FileOutputStream os=new FileOutputStream(out);
                rar.extractFile(fh,os);
                os.close();

                fh=rar.nextFileHeader();
            }

            rar.close();

            CheckBoxResponse checkBoxResponse = new CheckBoxResponse();
            scoreService.saveFileInDataBase(this.fileStorageLocation.resolve("NAMES.xlsx").toString(), checkBoxResponse);
            creditOrganizationService.saveFileInDataBase(this.fileStorageLocation.resolve("092018N1.xlsx").toString(), checkBoxResponse);
            userDataService.deleteDataInDataBase();
            userDataService.saveFileInDataBase(this.fileStorageLocation.resolve("092018B1.xlsx").toString(), checkBoxResponse);
            logger.info("Успешно разархивировали RAR файла и записали файлы в базу");
            return checkBoxResponse;
        } catch (IOException  ex) {
            cleanDirectory();
            throw new FileStorageException("Failed to save the contents of the rar file: " + fileName + ". Please try again!", ex);
        } catch (RarException ex) {
            throw new FileStorageException("Failed to save the contents of the rar file: " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void setListUserDataFilter(List<Long> creditOrganizationId, List<Long> scoreId, List<Long> userDataTitle) {
        logger.info("Начали запись фильтров в файл");
        try (Workbook book = new XSSFWorkbook(); OutputStream outputStream = new FileOutputStream(this.fileStorageLocation.resolve("ListUserDataFilter.xlsx").toString())) {
            Sheet sheet = book.createSheet("Data");
            Row row0 = sheet.createRow(0);
            Row row1 = sheet.createRow(1);
            Row row2 = sheet.createRow(2);

            for (int i = 0; i < creditOrganizationId.size(); i++) {
                row0.createCell(i).setCellValue(creditOrganizationId.get(i));
            }
            for (int i = 0; i < scoreId.size(); i++) {
                row1.createCell(i).setCellValue(scoreId.get(i));
            }
            for (int i = 0; i < userDataTitle.size(); i++) {
                row2.createCell(i).setCellValue(userDataTitle.get(i));
            }

            book.write(outputStream);
            logger.info("Успешно записали фильтры в файл");
        } catch (IOException ex) {
            cleanDirectory();
            throw new FileStorageException("Could not store file ListUserDataFilter.xlsx. Please try again!", ex);
        }
    }

    @Override
    public List<Long> getListUserDataFilter(int rowNumber) {
        logger.info("Начали получать фильтры из файла");
        try (FileInputStream excelFile = new FileInputStream(new File(this.fileStorageLocation.resolve("ListUserDataFilter.xlsx").toString()));
              XSSFWorkbook workbook = new XSSFWorkbook(excelFile)){

            List<Long> dataFilter = new ArrayList<>();
            Row row = workbook.getSheetAt(0).getRow(rowNumber);

            for(Cell cell : row){
                dataFilter.add((long)cell.getNumericCellValue());
            }

            logger.info("Успешно получили фильтры из файла");
           return dataFilter;
        } catch (IOException ex) {
            throw new FileStorageException("Could not get data from file ListUserDataFilter.xlsx. Please try again!", ex);
        }
    }

    public void cleanDirectory() {
        try {
            FileUtils.cleanDirectory(fileStorageLocation.toFile());
        } catch (IOException ex) {
            throw new FileStorageException("Could not clean the directory: " + fileStorageLocation.toString(), ex);
        }
    }
}
