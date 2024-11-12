package io.dbeauregard.pivotalspring.restapi;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.dbeauregard.pivotalspring.HouseEntity;
import io.dbeauregard.pivotalspring.HouseRepository;

@RestController
public class HouseController {

    private static Logger log = LoggerFactory.getLogger(HouseController.class);
    private final HouseRepository repo;

    HouseController(HouseRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/houses/{id}")
    EntityModel<House> getHouse(@PathVariable Long id) {
        log.info("Called getHouse() on {}.", id);
        HouseEntity he = findHouseByIdorException(id);
        return generateEntityModel(new House(he));
    }

    @GetMapping("/houses")
    CollectionModel<EntityModel<House>> getAllHouses() {
        log.info("Called getHouses()...");
        Iterable<HouseEntity> heList = repo.findAll();
        List<EntityModel<House>> hDTOList = new ArrayList<EntityModel<House>>();
        heList.forEach(he -> 
            hDTOList.add(generateEntityModelforList(new House(he)))
        );
        return CollectionModel.of(hDTOList, linkTo(methodOn(HouseController.class).getAllHouses()).withSelfRel());
    }

    @PostMapping("/houses")
    EntityModel<House> addHouse(@RequestBody House house) {
        log.info("Called addHouse() with house: {}", house);
        HouseEntity he = repo.save(house.getEntity());
        return generateEntityModel(new House(he));
    }

    @PutMapping("/houses/{id}")
    EntityModel<House> updateHouse(@PathVariable Long id, @RequestBody House updatedHouse) {
        log.info("Called updateHouse() on {} with house {}", id, updatedHouse);
        if (updatedHouse.getId() == null || !updatedHouse.getId().equals(id)) {
            log.info("House is missing ID or ID missmatch. ID {}, House {}.", id, updatedHouse);
            throw new InvalidRequestException(
                    String.format("House is missing ID or ID missmatch. ID %s, House %s.", id, updatedHouse));
        }

        HouseEntity oldHouse = findHouseByIdorException(id);
        oldHouse.setAddress(updatedHouse.getAddress());
        oldHouse.setPrice(updatedHouse.getPrice());

        HouseEntity he = repo.save(oldHouse);
        return generateEntityModel(new House(he));
    }

    @DeleteMapping("/houses/{id}")
    void deleteHouse(@PathVariable Long id) {
        log.info("Called deleteHouse() on {}.", id);
        findHouseByIdorException(id);
        repo.deleteById(id);
    }

    private HouseEntity findHouseByIdorException(Long id) {
        return repo.findById(id).orElseThrow(() -> new HouseNotFoundException(id));
    }

    private EntityModel<House> generateEntityModelforList(House h) {
        return EntityModel.of(h,
                linkTo(methodOn(HouseController.class).getHouse(h.getId())).withSelfRel());
    }

    private EntityModel<House> generateEntityModel(House h) {
        return EntityModel.of(h,
                linkTo(methodOn(HouseController.class).getHouse(h.getId())).withSelfRel(),
                linkTo(methodOn(HouseController.class).getAllHouses()).withRel("houses"));
    }

}
