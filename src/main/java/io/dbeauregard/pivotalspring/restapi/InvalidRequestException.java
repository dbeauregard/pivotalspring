package io.dbeauregard.pivotalspring;

public class InvalidRequestException extends RuntimeException {

    InvalidRequestException (String message) {
        super(message);
    }

}
