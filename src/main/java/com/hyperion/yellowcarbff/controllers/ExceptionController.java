package com.hyperion.yellowcarbff.controllers;

import com.hyperion.yellowcarbff.exceptions.DuplicateUserException;
import com.hyperion.yellowcarbff.exceptions.InvalidEmailException;
import com.hyperion.yellowcarbff.exceptions.InvalidTokenException;
import com.hyperion.yellowcarbff.model.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    protected ErrorResponse handleDuplicateUser(DuplicateUserException exception) {
        return ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .cause(exception.getCause() != null ? exception.getCause().toString() : "")
                .build();
    }

    @ExceptionHandler(value = {InvalidTokenException.class, InvalidEmailException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorResponse handleInvalidException(RuntimeException exception) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .cause(exception.getCause() != null ? exception.getCause().toString() : "")
                .build();
    }

}
