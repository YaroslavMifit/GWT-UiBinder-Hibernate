package com.example.filedemo.model;


import com.example.filedemo.repository.ScoreRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ScoreTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    public void whenFindByName_thenReturnScore() {
        // given
        Score score = new Score();
        score.setId(11443522L).setScoreName("Расходы от переоценки ценных бумаг");
        entityManager.persist(score);
        entityManager.flush();

        // when
        Score found = scoreRepository.getOne(score.getId());

        // then
        Assert.assertEquals(found.getScoreName(), score.getScoreName());
    }
}