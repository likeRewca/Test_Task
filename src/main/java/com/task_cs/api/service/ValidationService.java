package com.task_cs.api.service;

import com.task_cs.api.exception.UserValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ValidationService {
    private final int validAge;

    public ValidationService(@Value("${validation.age}") int validAge) {
        this.validAge = validAge;
    }

    public void validateDate(LocalDate birthday) {
        if (LocalDate.now().isEqual(birthday) || LocalDate.now().isBefore(birthday)) {
            throw new UserValidationException("Invalid date");
        }
    }

    public void validateAge(LocalDate birthday) {
        if (LocalDate.now().isBefore(birthday.plusYears(validAge))) {
            throw new UserValidationException(String.format("You are under the age %s", validAge));
        }
    }
}
