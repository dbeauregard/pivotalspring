package io.dbeauregard.pivotalspring;

public class HouseNotFoundException extends RuntimeException {

    HouseNotFoundException (Long id) {
        super("Could not find house " + id);
    }

}
