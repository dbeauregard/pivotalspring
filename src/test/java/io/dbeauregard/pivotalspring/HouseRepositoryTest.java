package io.dbeauregard.pivotalspring;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HouseRepositoryTest {

    private final HouseRepository repo;

    HouseRepositoryTest(HouseRepository repo) {
        this.repo = repo;
    }

    //Smoke Test Entity and wiring to in memory database.  Not testing Spring JPA/Hybernate.

}
