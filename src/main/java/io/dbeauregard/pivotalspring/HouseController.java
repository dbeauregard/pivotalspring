package io.dbeauregard.pivotalspring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
class HouseController {
    
    private static Logger log = LoggerFactory.getLogger(HouseController.class);

    @Autowired
    private HouseRepository repo;

    @GetMapping("/houses")
    Iterable<HouseEntity> getHouses() {
        log.info("Called getHouses()...");
        return repo.findAll();
    }

}
