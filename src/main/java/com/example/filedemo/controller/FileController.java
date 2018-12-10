package com.example.filedemo.controller;

import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public CheckBoxResponse uploadFile(@RequestParam("file") MultipartFile file) {
        return fileStorageService.storeFile(file);
    }

    @PostMapping(value = "/userDataReport")
    @ResponseBody
    public ResponseEntity<Void> saveFilterData(@RequestParam("creditOrganizationId") List<Long> creditOrganizationId,
                                               @RequestParam("scoreId") List<Long> scoreId,
                                               @RequestParam("userDataTitle") List<Long> userDataTitle){

        fileStorageService.setListUserDataFilter(creditOrganizationId, scoreId, userDataTitle);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
