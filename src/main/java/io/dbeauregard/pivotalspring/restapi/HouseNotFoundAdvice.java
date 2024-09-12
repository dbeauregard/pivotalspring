package io.dbeauregard.pivotalspring.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HouseNotFoundAdvice {
   
    @ExceptionHandler(HouseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(HouseNotFoundException ex) {
        return ex.getMessage();
    }

}
