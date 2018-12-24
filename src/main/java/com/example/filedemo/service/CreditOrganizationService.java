package com.example.filedemo.service;

import com.example.filedemo.model.CheckBoxResponse;

public interface CreditOrganizationService {
    void saveFileInDataBase(String filePath, CheckBoxResponse checkBoxResponse);
}
