package com.example.filedemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class CheckBoxResponse {
    private Set<CreditOrganization> creditOrganization;
    private Set<Score> Score;
    private List<String> userDataTitle;
}
