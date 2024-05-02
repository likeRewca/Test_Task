package com.task_cs.api.controller.handler;

import com.task_cs.api.exception.UserNotFoundException;
import com.task_cs.api.exception.UserValidationException;
import com.task_cs.api.model.dto.exception.ExceptionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import static org.junit.jupiter.api.Assertions.*;


class ExceptionHandlerControllerTest {

    private ExceptionHandlerController exceptionHandlerController;

    @BeforeEach
    void init() {
        exceptionHandlerController = new ExceptionHandlerController();
    }

    @Test
    void handleUserValidationExceptionWithInvalidDate() {
        String message = "Invalid date";

        UserValidationException exception = new UserValidationException(message);

        ExceptionDTO expectedResult = exceptionHandlerController.handleUserValidationException(exception);

        assertEquals(expectedResult.getErrorCode(), BAD_REQUEST.value());
        assertEquals(expectedResult.getErrorMessage(), exception.getMessage());
        assertEquals(expectedResult.getErrorTitle(), BAD_REQUEST.name());
    }

    @Test
    void handleUserValidationExceptionWithInvalidAge() {
        String message = String.format("You are under the age %s", 18);

        UserValidationException exception = new UserValidationException(message);

        ExceptionDTO expectedResult = exceptionHandlerController.handleUserValidationException(exception);

        assertEquals(expectedResult.getErrorCode(), BAD_REQUEST.value());
        assertEquals(expectedResult.getErrorMessage(), exception.getMessage());
        assertEquals(expectedResult.getErrorTitle(), BAD_REQUEST.name());
    }

    @Test
    void handleUserNotFoundException() {
        Integer userId = 18;
        String message = String.format("Can`t find user with id = %s", userId);;

        UserNotFoundException exception = new UserNotFoundException(message);

        ExceptionDTO expectedResult = exceptionHandlerController.handleUserNotFoundException(exception);

        assertEquals(expectedResult.getErrorCode(), NOT_FOUND.value());
        assertEquals(expectedResult.getErrorMessage(), exception.getMessage());
        assertEquals(expectedResult.getErrorTitle(), NOT_FOUND.name());
    }
}