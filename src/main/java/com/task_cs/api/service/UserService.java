package com.task_cs.api.service;

import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;

import java.time.LocalDate;
import java.util.List;


public interface UserService {

    UserResponseDto getUser(Integer id);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto);

    void deleteUser(Integer id);

    List<UserResponseDto> searchUser(LocalDate from, LocalDate to);
}
