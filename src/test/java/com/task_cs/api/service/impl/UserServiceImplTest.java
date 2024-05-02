package com.task_cs.api.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_cs.api.exception.UserNotFoundException;
import com.task_cs.api.mapper.UserMapper;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.model.entity.UserEntity;
import com.task_cs.api.presets.UserPresets;
import com.task_cs.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUser() {
        Integer userId = 1;

        UserEntity entity = UserPresets.userEntityPreset();
        UserResponseDto actualResult = UserPresets.userResponseDto();

        when(repository.findById(userId)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDto(entity)).thenReturn(actualResult);

        UserResponseDto expectedResult = userService.getUser(userId);

        verify(repository).findById(userId);
        verify(mapper).toResponseDto(entity);

        assertEquals(expectedResult.getId(), userId);
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getFirstName(), actualResult.getFirstName());
        assertEquals(expectedResult.getLastName(), actualResult.getLastName());
        assertEquals(expectedResult.getBirthday(), actualResult.getBirthday());
        assertEquals(expectedResult.getAddress(), actualResult.getAddress());
        assertEquals(expectedResult.getPhoneNumber(), actualResult.getPhoneNumber());
    }

    @Test
    void testGetUserAndCatchUserNotFoundException() {
        Integer userId = 22;

        when(repository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(userId));

        String expectedMessage = String.format("Can`t find user with id = %s", userId);
        String actualMessage = exception.getMessage();

        verify(repository).findById(userId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testCreateUser() {
        UserRequestDto requestDto = UserPresets.userRequestDtoPreset();
        UserEntity entity = UserPresets.userEntityPreset();
        UserResponseDto actualResult = UserPresets.userResponseDto();

        when(mapper.toEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponseDto(entity)).thenReturn(actualResult);

        UserResponseDto expectedResult = userService.createUser(requestDto);

        verify(mapper).toEntity(requestDto);
        verify(repository).save(entity);
        verify(mapper).toResponseDto(entity);

        assertEquals(expectedResult.getEmail(), requestDto.getEmail());
        assertEquals(expectedResult.getFirstName(), requestDto.getFirstName());
        assertEquals(expectedResult.getLastName(), requestDto.getLastName());
        assertEquals(expectedResult.getBirthday(), requestDto.getBirthday());
        assertEquals(expectedResult.getAddress(), requestDto.getAddress());
        assertEquals(expectedResult.getPhoneNumber(), requestDto.getPhoneNumber());
    }

    @Test
    void testUpdateUser() throws JsonMappingException {
        Integer userId = 1;

        UserRequestDto requestDto = UserPresets.userRequestDtoPreset();
        UserEntity entity = UserPresets.userEntityPreset();
        UserResponseDto actualResult = UserPresets.userResponseDto();

        when(repository.findById(userId)).thenReturn(Optional.of(entity));
        //when(objectMapper.updateValue(entity, requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponseDto(entity)).thenReturn(actualResult);

        UserResponseDto expectedResult = userService.updateUser(userId, requestDto);

        verify(repository).findById(userId);
        //verify(objectMapper).updateValue(entity, requestDto);
        verify(repository).save(entity);
        verify(mapper).toResponseDto(entity);

        assertEquals(expectedResult.getId(), userId);
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getFirstName(), actualResult.getFirstName());
        assertEquals(expectedResult.getLastName(), actualResult.getLastName());
        assertEquals(expectedResult.getBirthday(), actualResult.getBirthday());
        assertEquals(expectedResult.getAddress(), actualResult.getAddress());
        assertEquals(expectedResult.getPhoneNumber(), actualResult.getPhoneNumber());
    }

    @Test
    void testUpdateUserAndCatchUserNotFoundException() {
        Integer userId = 21;

        when(repository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(userId));

        String expectedMessage = String.format("Can`t find user with id = %s", userId);
        String actualMessage = exception.getMessage();

        verify(repository).findById(userId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testDeleteUser() {
        Integer userId = 1;

        UserEntity entity = UserPresets.userEntityPreset();

        when(repository.findById(userId)).thenReturn(Optional.of(entity));
        doNothing().when(repository).delete(entity);

        userService.deleteUser(userId);

        verify(repository).findById(userId);
        verify(repository).delete(entity);
    }

    @Test
    void testDeleteUserAndCatchUserNotFoundException() {
        Integer userId = 1;

        when(repository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(userId));

        String expectedMessage = String.format("Can`t find user with id = %s", userId);
        String actualMessage = exception.getMessage();

        verify(repository).findById(userId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testSearchUser() {
        LocalDate from = LocalDate.now().minusYears(19);
        LocalDate to = LocalDate.now().minusYears(18);

        List<UserEntity> users = UserPresets.listOfUserEntityPreset();
        List<UserResponseDto> actualResult = UserPresets.listOfUserResponseDtoPreset();

        when(repository.findByBirthdayBetween(from, to)).thenReturn(users);

        List<UserResponseDto> expectedListOfUsers = userService.searchUser(from, to);

        verify(repository).findByBirthdayBetween(from, to);

        assertEquals(expectedListOfUsers.size(), actualResult.size());
    }
}