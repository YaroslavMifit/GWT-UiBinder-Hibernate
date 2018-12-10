package com.example.filedemo.repository;

import com.example.filedemo.model.CreditOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditOrganizationRepository extends JpaRepository<CreditOrganization, Long> {
}
