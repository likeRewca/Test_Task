package com.task_cs.api.mapper;

import com.task_cs.api.model.UserRequest;
import com.task_cs.api.model.UserResponse;
import com.task_cs.api.model.UserUpdateRequest;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "firstName", source = "dto.firstName")
    @Mapping(target = "lastName", source = "dto.lastName")
    @Mapping(target = "birthday", source = "dto.birthday")
    @Mapping(target = "address", source = "dto.address")
    @Mapping(target = "phoneNumber", source = "dto.phoneNumber")
    UserEntity toEntity(UserRequestDto dto);

    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "birthday", source = "request.birthday")
    @Mapping(target = "address", source = "request.address")
    @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    UserRequestDto toRequestDto(UserRequest request);

    @Mapping(target = "email", source = "updateReq.email")
    @Mapping(target = "firstName", source = "updateReq.firstName")
    @Mapping(target = "lastName", source = "updateReq.lastName")
    @Mapping(target = "birthday", source = "updateReq.birthday")
    @Mapping(target = "address", source = "updateReq.address")
    @Mapping(target = "phoneNumber", source = "updateReq.phoneNumber")
    UserRequestDto toUpdateDto(UserUpdateRequest updateReq);

    @Mapping(target = "email", source = "entity.email")
    @Mapping(target = "firstName", source = "entity.firstName")
    @Mapping(target = "lastName", source = "entity.lastName")
    @Mapping(target = "birthday", source = "entity.birthday")
    @Mapping(target = "address", source = "entity.address")
    @Mapping(target = "phoneNumber", source = "entity.phoneNumber")
    UserResponseDto toResponseDto(UserEntity entity);

    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "firstName", source = "dto.firstName")
    @Mapping(target = "lastName", source = "dto.lastName")
    @Mapping(target = "birthday", source = "dto.birthday")
    @Mapping(target = "address", source = "dto.address")
    @Mapping(target = "phoneNumber", source = "dto.phoneNumber")
    UserResponse toResponse(UserResponseDto dto);
}