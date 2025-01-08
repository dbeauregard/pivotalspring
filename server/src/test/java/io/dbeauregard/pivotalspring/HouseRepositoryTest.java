package io.dbeauregard.pivotalspring;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

@DataJpaTest
public class HouseRepositoryTest {

    private final HouseRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    HouseRepositoryTest(HouseRepository repo) {
        this.repo = repo;
    }

    // Smoke Test Entity and wiring to in memory database. Not testing Spring
    // JPA/Hybernate.
    @Test
    public void smokeTestRepo() {
        //Find All
        HouseEntity he = new HouseEntity("5678", 1234, 3, 2, 2000);
        entityManager.persist(he);
        Iterable<HouseEntity> resultList = repo.findAll();
        assertNotNull(resultList);
        assertEquals(Iterables.size(resultList),11);
        
        //Save One
        HouseEntity result = repo.save(new HouseEntity("1234", 56789, 3, 2, 2000));
        assertNotNull(result.getId());
        assertNotNull(repo.findById(result.getId()));

        //Update One
        result.setAddress("5678");
        result = repo.save(new HouseEntity("1234", 56789, 2, 2, 1500));
        assertNotNull(repo.findAll());
        assertNotNull(result.getId());
        
        //Delete One
        repo.delete(result);
    }

}
