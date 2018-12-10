package com.example.filedemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Entity
// Файл NAMES.xlsx
@Table(name = "score")
public class Score {

    //Номер счета второго порядка по Плану счетов
    @Id
    @Column(name = "id")
    private long id;

    //Наименование счета второго порядка
    @Column(name = "score_name")
    private String scoreName;
}
