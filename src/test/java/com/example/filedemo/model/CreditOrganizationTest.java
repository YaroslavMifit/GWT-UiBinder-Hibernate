package com.example.filedemo.model;

import com.example.filedemo.repository.CreditOrganizationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CreditOrganizationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CreditOrganizationRepository creditOrganizationRepository;

    @Test
    public void whenFindByName_thenReturnCreditOrganization() {
        // given
        CreditOrganization creditOrganization = new CreditOrganization();
        creditOrganization.setId(114352L).setCreditOrganizationName("Сбербанк");
        entityManager.persist(creditOrganization);
        entityManager.flush();

        // when
        CreditOrganization found = creditOrganizationRepository.getOne(creditOrganization.getId());

        // then
        Assert.assertEquals(found.getCreditOrganizationName(), creditOrganization.getCreditOrganizationName());
    }
}