package com.example.filedemo.model;

import com.example.filedemo.repository.UserDataRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDataRepository userDataRepository;

    @Test
    public void whenFindByName_thenReturnUserData() {
        // given
        UserData userData = new UserData();
        userData.setIncomingBalancesInRubles("234575").setIncomingBalancesDragMetals("3435246").setIncomingBalancesOfTotal("3424245");
        entityManager.persist(userData);
        entityManager.flush();

        // when
        UserData found = userDataRepository.getOne(userData.getId());

        // then
        Assert.assertEquals(found.getIncomingBalancesInRubles(), userData.getIncomingBalancesInRubles());
    }

}