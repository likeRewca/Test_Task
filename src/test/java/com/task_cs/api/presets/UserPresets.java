package com.task_cs.api.presets;

import com.task_cs.api.model.UserRequest;
import com.task_cs.api.model.UserUpdateRequest;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.model.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;

public class UserPresets {
    public static final Integer USER_ID = 1;
    public static final Integer USER_AGE = 18;
    private static final String EMAIL = "example@gmail.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final LocalDate BIRTHDAY = LocalDate.now().minusYears(USER_AGE);
    private static final String ADDRESS = "Wall-street 22";
    private static final String PHONE = "564654651231";

    public static UserRequestDto userRequestDtoPreset() {
        return UserRequestDto.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthday(BIRTHDAY)
                .address(ADDRESS)
                .phoneNumber(PHONE)
                .build();
    }

    public static UserRequest userRequestPreset() {
        return UserRequest.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthday(BIRTHDAY)
                .address(ADDRESS)
                .phoneNumber(PHONE)
                .build();
    }

    public static UserUpdateRequest userUpdateRequestPreset() {
        return UserUpdateRequest.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthday(BIRTHDAY)
                .address(ADDRESS)
                .phoneNumber(PHONE)
                .build();
    }

    public static UserEntity userEntityPreset() {
        return UserEntity.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthday(BIRTHDAY)
                .address(ADDRESS)
                .phoneNumber(PHONE)
                .build();
    }

    public static UserResponseDto userResponseDto() {
        return UserResponseDto.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthday(BIRTHDAY)
                .address(ADDRESS)
                .phoneNumber(PHONE)
                .build();
    }

    public static List<UserEntity> listOfUserEntityPreset() {
        UserEntity firstUser = UserEntity.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthday(BIRTHDAY)
                .address(ADDRESS)
                .phoneNumber(PHONE)
                .build();

        UserEntity secondUser = UserEntity.builder()
                .id(2)
                .email("example@example.com")
                .firstName("Tom")
                .lastName("Potter")
                .birthday(LocalDate.now().minusYears(19))
                .address("1st Street")
                .phoneNumber("082132153154")
                .build();

        return List.of(firstUser, secondUser);
    }

    public static List<UserResponseDto> listOfUserResponseDtoPreset() {
        UserResponseDto firstUser = UserResponseDto.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthday(BIRTHDAY)
                .address(ADDRESS)
                .phoneNumber(PHONE)
                .build();

        UserResponseDto secondUser = UserResponseDto.builder()
                .id(2)
                .email("example@example.com")
                .firstName("Tom")
                .lastName("Potter")
                .birthday(LocalDate.now().minusYears(19))
                .address("1st Street")
                .phoneNumber("082132153154")
                .build();

        return List.of(firstUser, secondUser);
    }
}
