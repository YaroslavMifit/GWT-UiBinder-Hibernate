package com.example.filedemo.service;

import com.example.filedemo.model.CheckBoxResponse;
import com.example.filedemo.model.UserData;

import java.util.List;

public interface UserDataService {
    List<UserData> getListUserDataFilter();
    void deleteDataInDataBase();
    void saveFileInDataBase(String filePath, CheckBoxResponse checkBoxResponse);

}
