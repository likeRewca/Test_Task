package com.task_cs.api.controller;

import com.task_cs.api.model.UserRequest;
import com.task_cs.api.model.UserResponse;
import com.task_cs.api.model.UserUpdateRequest;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.mapper.UserMapper;
import com.task_cs.api.model.entity.UserEntity;
import com.task_cs.api.service.UserService;
import com.task_cs.api.service.ValidationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ValidationService validationService;

    private final UserMapper mapper;


    public UserController(UserService userService, ValidationService validationService, UserMapper mapper) {
        this.userService = userService;
        this.validationService = validationService;
        this.mapper = mapper;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id) {

        UserResponseDto responseDto = userService.getUser(id);

        return ResponseEntity.ok(mapper.toResponse(responseDto));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        validationService.validateDate(userRequest.getBirthday());
        validationService.validateAge(userRequest.getBirthday());

        UserRequestDto requestDto = mapper.toRequestDto(userRequest);

        UserResponseDto responseDto = userService.createUser(requestDto);

        return new ResponseEntity<>(mapper.toResponse(responseDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id,
                                                   @RequestBody UserUpdateRequest userRequest) {
        if(userRequest.getBirthday() != null) {
            validationService.validateDate(userRequest.getBirthday());
            validationService.validateAge(userRequest.getBirthday());
        }

        UserRequestDto requestDto = mapper.toUpdateDto(userRequest);

        UserResponseDto responseDto = userService.updateUser(id, requestDto);

        return ResponseEntity.ok(mapper.toResponse(responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> searchUsersByAge(@RequestParam(value = "from") LocalDate from,
                                                         @RequestParam(value = "to") LocalDate to) {

        List<UserResponseDto> responseDto = userService.searchUser(from, to);

        return ResponseEntity.ok(responseDto.stream().map(mapper::toResponse).toList());
    }
}
