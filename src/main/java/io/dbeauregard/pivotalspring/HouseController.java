package io.dbeauregard.pivotalspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
class HouseController {
    
    private static Logger log = LoggerFactory.getLogger(HouseController.class);

    @Autowired
    private HouseRepository repo;

    @PostMapping("/house/{id}")
    void addHouse(@PathVariable Long id) {
        log.info("Called addHouse() on {}.", id);
    }

    @PutMapping("/house/{id}")
    void updateHouse(@PathVariable Long id) {
        log.info("Called updateHouse() on {}.", id);
    }

    @GetMapping("/house/{id}")
    ResponseEntity<HouseEntity> getHouse(@PathVariable Long id) {
        log.info("Called getHouse() on {}.", id);
        return ResponseEntity.ok(repo.findById(id).orElse(null));
        // return repo.findById(id).orElse(null);
    }

    @DeleteMapping("/house/{id}")
    void deleteHouse(@PathVariable Long id) {
        log.info("Called deleteHouse() on {}.", id);
    }

    @GetMapping("/houses")
    Iterable<HouseEntity> getAllHouses() {
        log.info("Called getHouses()...");
        return repo.findAll();
    }

}
