package com.example.filedemo.service;

import com.example.filedemo.model.CheckBoxResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    CheckBoxResponse storeFile(MultipartFile file);
    void setListUserDataFilter(List<Long> creditOrganizationId, List<Long> scoreId, List<Long> userDataTitle);
    List<Long> getListUserDataFilter(int rowNumber);
    void cleanDirectory();
}
