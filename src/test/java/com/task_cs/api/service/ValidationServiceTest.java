package com.task_cs.api.service;

import com.task_cs.api.exception.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    private ValidationService validationService;

    private final int validAge = 18;

    @BeforeEach
    void init() {
        validationService = new ValidationService(validAge);
    }

    @Test
    void testValidationDate() {
        ReflectionTestUtils.setField(validationService, "validAge", validAge);
        LocalDate userBirthday = LocalDate.now().minusYears(validAge);

        validationService.validateDate(userBirthday);
    }

    @Test
    void testValidationDateWithCurrentDate() {
        LocalDate userBirthday = LocalDate.now();

        UserValidationException exception =
                assertThrows(UserValidationException.class, () -> validationService.validateDate(userBirthday));

        String expectedMessage = "Invalid date";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testValidationDateWithFutureDate() {
        LocalDate userBirthday = LocalDate.now().plusDays(5);

        UserValidationException exception =
                assertThrows(UserValidationException.class, () -> validationService.validateDate(userBirthday));

        String expectedMessage = "Invalid date";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testValidationOfUserAge() {
        LocalDate userBirthday = LocalDate.now().minusYears(validAge);

        validationService.validateAge(userBirthday);
    }

    @Test
    void testValidationUserAgeWithInvalidAge() {
        int invalidAge = 10;
        LocalDate userBirthday = LocalDate.now().minusYears(invalidAge);

        String expectedMessage = String.format("You are under the age %s", validAge);

        UserValidationException exception =
                assertThrows(UserValidationException.class, () -> validationService.validateAge(userBirthday));

        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}