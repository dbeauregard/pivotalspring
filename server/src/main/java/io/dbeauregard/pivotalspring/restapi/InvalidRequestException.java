package io.dbeauregard.pivotalspring.restapi;

public class InvalidRequestException extends RuntimeException {

    InvalidRequestException (String message) {
        super(message);
    }

}
