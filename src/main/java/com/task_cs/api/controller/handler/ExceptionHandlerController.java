package com.task_cs.api.controller.handler;

import com.task_cs.api.exception.UserNotFoundException;
import com.task_cs.api.model.dto.exception.ExceptionDTO;
import com.task_cs.api.exception.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserValidationException.class)
    public ExceptionDTO handleUserValidationException(UserValidationException ex) {
        return buildUserExceptionDTO(ex, BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ExceptionDTO handleUserNotFoundException(UserNotFoundException ex) {
        return buildUserExceptionDTO(ex, NOT_FOUND);
    }

    @ResponseStatus(value = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionDTO handleUserValidationFieldsException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String message = fieldError != null ? fieldError.getDefaultMessage() : "Invalid input data";

        return ExceptionDTO.builder()
                        .errorCode(BAD_REQUEST.value())
                        .errorTitle(BAD_REQUEST.name())
                        .errorMessage(message)
                        .errorTimestamp(LocalDateTime.now())
                        .build();
    }

    private <T extends RuntimeException> ExceptionDTO buildUserExceptionDTO(T exception, HttpStatus status) {
        return ExceptionDTO.builder()
                .errorCode(status.value())
                .errorTitle(status.name())
                .errorMessage(exception.getMessage())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }
}
