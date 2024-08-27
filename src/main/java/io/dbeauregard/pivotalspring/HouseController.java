package io.dbeauregard.pivotalspring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HouseController {
    
    @GetMapping("/houses")
    public House getHouses() {
        return new House(Integer.valueOf(1),"asdf",Integer.valueOf(199));
    }

}
