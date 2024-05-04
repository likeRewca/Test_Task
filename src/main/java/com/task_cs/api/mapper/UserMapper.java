package com.task_cs.api.mapper;

import com.task_cs.api.model.UserRequest;
import com.task_cs.api.model.UserResponse;
import com.task_cs.api.model.UserUpdateRequest;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserRequestDto dto);

    UserRequestDto toRequestDto(UserRequest request);

    UserRequestDto toUpdateDto(UserUpdateRequest updateReq);

    UserResponseDto toResponseDto(UserEntity entity);

    UserResponse toResponse(UserResponseDto dto);
}