package com.task_cs.api.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.model.entity.UserEntity;
import com.task_cs.api.repository.UserRepository;
import com.task_cs.api.mapper.UserMapper;
import com.task_cs.api.service.UserService;
import com.task_cs.api.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, ObjectMapper objectMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserResponseDto getUser(Integer id) {

        UserEntity user = repository
                .findById(id).orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Can`t find user with id = %s", id)));

        return mapper.toResponseDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        UserEntity user = mapper.toEntity(userRequestDto);

        repository.save(user);

        return mapper.toResponseDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) {
        UserEntity user = repository
                .findById(id).orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Can`t find user with id = %s", id)));

        try {
            objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.updateValue(user, userRequestDto);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }

        repository.save(user);
        return mapper.toResponseDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Integer id) {
        UserEntity user = repository
                .findById(id).orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Can`t find user with id = %s", id)));

        repository.delete(user);
    }

    @Transactional
    @Override
    public List<UserResponseDto> searchUser(LocalDate from, LocalDate to) {

        List<UserEntity> users = repository.findByBirthdayBetween(from, to);

        return users.stream()
                .map(this::toResponseDto)
                .toList();
    }

    private UserResponseDto toResponseDto(UserEntity entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthday(entity.getBirthday())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .build();

    }
}
