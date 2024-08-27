package io.dbeauregard.pivotalspring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HouseController {
    
    static Logger log = LoggerFactory.getLogger(HouseController.class);

    @GetMapping("/houses")
    public List<HouseEntity> getHouses() {
        log.info("Called getHouses()...");
        List<HouseEntity> houseList = new ArrayList<HouseEntity>();
        houseList.add(new HouseEntity("199 drive", Integer.valueOf(199)));
        houseList.add(new HouseEntity("1235 street", Integer.valueOf(222)));
        return houseList;
    }

}
