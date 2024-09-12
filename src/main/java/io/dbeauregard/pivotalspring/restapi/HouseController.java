package io.dbeauregard.pivotalspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HouseController {
    
    private static Logger log = LoggerFactory.getLogger(HouseController.class);
    private final HouseRepository repo;
    
    HouseController(HouseRepository repo) {
        this.repo = repo;
    }

    private HouseEntity findHouseByIdorException(Long id) {
        return repo.findById(id).orElseThrow(() -> new HouseNotFoundException(id));
    }

    @PostMapping("/house")
    HouseEntity  addHouse(@RequestBody HouseEntity house) {
        log.info("Called addHouse() with house: {}", house);
        return repo.save(house);
    }

    @PutMapping("/house/{id}")
    HouseEntity updateHouse(@PathVariable Long id, @RequestBody HouseEntity newHouse) {
        log.info("Called updateHouse() on {} with house {}", id, newHouse);
        if (newHouse.getId() == null || !newHouse.getId().equals(id)) {
            log.info("House is missing ID or ID missmatch. ID {}, House {}.", id, newHouse);
            throw new InvalidRequestException(String.format("House is missing ID or ID missmatch. ID %s, House %s.", id, newHouse));
        }

        HouseEntity oldHouse = findHouseByIdorException(id);
        oldHouse.setAddress(newHouse.getAddress());
        oldHouse.setPrice(newHouse.getPrice());
        return repo.save(oldHouse);
    }

    @GetMapping("/house/{id}")
    HouseEntity getHouse(@PathVariable Long id) {
        log.info("Called getHouse() on {}.", id);
        return findHouseByIdorException(id);
    }

    @DeleteMapping("/house/{id}")
    void deleteHouse(@PathVariable Long id) {
        log.info("Called deleteHouse() on {}.", id);
        findHouseByIdorException(id);
        repo.deleteById(id);
    }

    @GetMapping("/houses")
    Iterable<HouseEntity> getAllHouses() {
        log.info("Called getHouses()...");
        return repo.findAll();
    }

}
