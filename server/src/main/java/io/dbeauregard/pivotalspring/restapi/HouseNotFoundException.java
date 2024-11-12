package io.dbeauregard.pivotalspring.restapi;

public class HouseNotFoundException extends RuntimeException {

    HouseNotFoundException (Long id) {
        super("Could not find house " + id);
    }

}
