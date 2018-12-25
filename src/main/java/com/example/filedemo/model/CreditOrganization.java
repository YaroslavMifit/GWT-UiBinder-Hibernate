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
// Файл 092018N1
@Table(name = "credit_organization")
public class CreditOrganization {

    //Регистрационный номер кредитной организации
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    //Наименование кредитной организации
    @Column(name = "credit_organization_name", nullable = false)
    private String creditOrganizationName;
}
