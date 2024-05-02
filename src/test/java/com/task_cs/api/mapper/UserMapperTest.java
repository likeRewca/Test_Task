package com.task_cs.api.mapper;

import com.task_cs.api.model.UserRequest;
import com.task_cs.api.model.UserResponse;
import com.task_cs.api.model.UserUpdateRequest;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.model.entity.UserEntity;
import com.task_cs.api.presets.UserPresets;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testFromUserRequestDtoToUserEntity() {
        UserRequestDto reqDto = UserPresets.userRequestDtoPreset();

        UserEntity entity = mapper.toEntity(reqDto);

        assertDataEquals(entity, reqDto);
    }

    @Test
    void testFromUserRequestToUserRequestDto() {
        UserRequest request = UserPresets.userRequestPreset();

        UserRequestDto reqDto = mapper.toRequestDto(request);

        assertDataEquals(reqDto, request);
    }

    @Test
    void testFromUserUpdateRequestToUserRequestDto() {
        UserUpdateRequest reqUpdateDto = UserPresets.userUpdateRequestPreset();

        UserRequestDto reqDto = mapper.toUpdateDto(reqUpdateDto);

        assertDataEquals(reqDto, reqUpdateDto);
    }

    @Test
    void testFromUserEntityToUserResponseDto() {
        UserEntity entity = UserPresets.userEntityPreset();

        UserResponseDto respDto = mapper.toResponseDto(entity);

        assertDataEquals(respDto, entity);
    }

    @Test
    void testFromUserResponseDtoToUserResponse() {
        UserResponseDto dto = UserPresets.userResponseDto();

        UserResponse response = mapper.toResponse(dto);

        assertDataEquals(response, dto);
    }

    private void assertDataEquals(UserEntity entity, UserRequestDto dto) {
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getBirthday(), dto.getBirthday());
        assertEquals(entity.getAddress(), dto.getAddress());
        assertEquals(entity.getPhoneNumber(), dto.getPhoneNumber());
    }

    private void assertDataEquals(UserRequestDto dto, UserRequest request) {
        assertEquals(dto.getEmail(), request.getEmail());
        assertEquals(dto.getFirstName(), request.getFirstName());
        assertEquals(dto.getLastName(), request.getLastName());
        assertEquals(dto.getBirthday(), request.getBirthday());
        assertEquals(dto.getAddress(), request.getAddress());
        assertEquals(dto.getPhoneNumber(), request.getPhoneNumber());
    }

    private void assertDataEquals(UserRequestDto dto, UserUpdateRequest reqUpdate) {
        assertEquals(dto.getEmail(), reqUpdate.getEmail());
        assertEquals(dto.getFirstName(), reqUpdate.getFirstName());
        assertEquals(dto.getLastName(), reqUpdate.getLastName());
        assertEquals(dto.getBirthday(), reqUpdate.getBirthday());
        assertEquals(dto.getAddress(), reqUpdate.getAddress());
        assertEquals(dto.getPhoneNumber(), reqUpdate.getPhoneNumber());
    }

    private void assertDataEquals(UserResponseDto dto, UserEntity entity) {
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getBirthday(), entity.getBirthday());
        assertEquals(dto.getAddress(), entity.getAddress());
        assertEquals(dto.getPhoneNumber(), entity.getPhoneNumber());
    }

    private void assertDataEquals(UserResponse response, UserResponseDto responseDto) {
        assertEquals(response.getEmail(), responseDto.getEmail());
        assertEquals(response.getFirstName(), responseDto.getFirstName());
        assertEquals(response.getLastName(), responseDto.getLastName());
        assertEquals(response.getBirthday(), responseDto.getBirthday());
        assertEquals(response.getAddress(), responseDto.getAddress());
        assertEquals(response.getPhoneNumber(), responseDto.getPhoneNumber());
    }
}