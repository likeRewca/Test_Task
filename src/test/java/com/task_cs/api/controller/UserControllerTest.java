package com.task_cs.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_cs.api.controller.handler.ExceptionHandlerController;
import com.task_cs.api.exception.UserNotFoundException;
import com.task_cs.api.exception.UserValidationException;
import com.task_cs.api.mapper.UserMapper;
import com.task_cs.api.mapper.UserMapperImpl;
import com.task_cs.api.model.UserRequest;
import com.task_cs.api.model.UserResponse;
import com.task_cs.api.model.UserUpdateRequest;
import com.task_cs.api.model.dto.UserRequestDto;
import com.task_cs.api.model.dto.UserResponseDto;
import com.task_cs.api.model.dto.exception.ExceptionDTO;
import com.task_cs.api.presets.UserPresets;
import com.task_cs.api.service.UserService;
import com.task_cs.api.service.ValidationService;
import com.task_cs.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
@Import(UserController.class)
@ContextConfiguration(
        classes = {UserServiceImpl.class, UserMapperImpl.class, ExceptionHandlerController.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ValidationService validationService;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private UserMapper mapper;

    @Test
    void testGetUserWhenValidInputAndReturns200() throws Exception {
        Integer userId = 1;
        UserResponseDto responseDto = UserPresets.userResponseDto();

        when(userService.getUser(userId)).thenReturn(responseDto);

        MvcResult mvcResult = mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        UserResponse expectedResponse = objectMapper.readValue(contentAsString, UserResponse.class);

        assertEquals(expectedResponse.getId(), responseDto.getId());
        assertEquals(expectedResponse.getEmail(), responseDto.getEmail());
        assertEquals(expectedResponse.getFirstName(), responseDto.getFirstName());
        assertEquals(expectedResponse.getLastName(), responseDto.getLastName());
        assertEquals(expectedResponse.getBirthday(), responseDto.getBirthday());
        assertEquals(expectedResponse.getAddress(), responseDto.getAddress());
        assertEquals(expectedResponse.getPhoneNumber(), responseDto.getPhoneNumber());
    }

    @Test
    void testCreateUserWhenValidInputAndReturns201() throws Exception {
        UserRequest request = UserPresets.userRequestPreset();
        UserRequestDto requestDto = UserPresets.userRequestDtoPreset();
        UserResponseDto responseDto = UserPresets.userResponseDto();

        doNothing().when(validationService).validateDate(request.getBirthday());
        doNothing().when(validationService).validateAge(request.getBirthday());

        when(userService.createUser(requestDto)).thenReturn(responseDto);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        UserResponse expectedResponse = objectMapper.readValue(contentAsString, UserResponse.class);

        assertEquals(expectedResponse.getId(), responseDto.getId());

        assertEquals(expectedResponse.getEmail(), request.getEmail());
        assertEquals(expectedResponse.getFirstName(), request.getFirstName());
        assertEquals(expectedResponse.getLastName(), request.getLastName());
        assertEquals(expectedResponse.getBirthday(), request.getBirthday());
        assertEquals(expectedResponse.getAddress(), request.getAddress());
        assertEquals(expectedResponse.getPhoneNumber(), request.getPhoneNumber());
    }

    @Test
    void testCreateUserWhenInvalidDateAndReturns400() throws Exception {
        UserRequest request = UserPresets.userRequestPreset();
        request.setBirthday(LocalDate.now());

        String exceptionMessage = "Invalid date";

        doThrow(new UserValidationException(exceptionMessage)).when(validationService).
                validateDate(request.getBirthday());

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(UserValidationException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(exceptionMessage,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void testCreateUserWhenInvalidUserAgeAndReturns400() throws Exception {
        int validAge = 18;
        int invalidUserAge = 10;

        String exceptionMessage = String.format("You are under the age %s", validAge);

        UserRequest request = UserPresets.userRequestPreset();
        request.setBirthday(LocalDate.now().minusYears(invalidUserAge));

        doNothing().when(validationService).validateDate(request.getBirthday());
        doThrow(new UserValidationException(exceptionMessage)).when(validationService).
                validateAge(request.getBirthday());

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(UserValidationException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(exceptionMessage,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(validationService).validateAge(request.getBirthday());
    }

    @Test
    void testCreateUserWhenInvalidUserEmailPatternAndReturns400() throws Exception {
        String invalidEmail = "example";
        String exceptionMessage = "Invalid email";

        UserRequest request = UserPresets.userRequestPreset();
        request.setEmail(invalidEmail);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                        result.getResolvedException()))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ExceptionDTO result = objectMapper.readValue(contentAsString, ExceptionDTO.class);

        assertEquals(result.getErrorMessage(), exceptionMessage);
        assertEquals(result.getErrorTitle(), "BAD_REQUEST");
        assertEquals(result.getErrorCode(), 400);
    }

    @Test
    void testCreateUserWhenInvalidUserPhonePatternAndReturns400() throws Exception {
        String invalidPhone = "56546546554";
        String exceptionMessage = "Invalid phone_number. Should be (xxx)xxx-xxxx or xxx-xxx-xxxx";

        UserRequest request = UserPresets.userRequestPreset();
        request.setPhoneNumber(invalidPhone);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                        result.getResolvedException()))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ExceptionDTO result = objectMapper.readValue(contentAsString, ExceptionDTO.class);

        assertEquals(result.getErrorMessage(), exceptionMessage);
        assertEquals(result.getErrorTitle(), "BAD_REQUEST");
        assertEquals(result.getErrorCode(), 400);
    }

    @ParameterizedTest
    @MethodSource("provideBlankUsersFields")
    void testCreateUserWhenBlankRequiredUserFieldsAndReturns400(String email, String firstName, String lastName,
                                                                LocalDate birthday, String exMessage) throws Exception {

        UserRequest request = UserRequest.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .address("Some Address")
                .phoneNumber("(099)999-9999")
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                        result.getResolvedException()))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ExceptionDTO result = objectMapper.readValue(contentAsString, ExceptionDTO.class);

        assertEquals(result.getErrorMessage(), exMessage);
        assertEquals(result.getErrorTitle(), "BAD_REQUEST");
        assertEquals(result.getErrorCode(), 400);
    }

    private static Stream<Arguments> provideBlankUsersFields() {
        return Stream.of(
                Arguments.of(null, "Nick", "Kant",
                        LocalDate.now().minusYears(20), "Email is mandatory field"),
                Arguments.of("example@example3.com", null, "Kant",
                        LocalDate.now().minusYears(20), "First name is mandatory field"),
                Arguments.of("example@example3.com", "Nick", null,
                        LocalDate.now().minusYears(20), "Last name is mandatory field"),
                Arguments.of("example@example3.com", "Nick", "Kant",
                        null, "Birthday is mandatory field")
        );
    }

    @Test
    void testUpdateUserWhenValidInputAndReturns200() throws Exception {
        Integer userId = 1;

        UserUpdateRequest request = UserPresets.userUpdateRequestPreset();
        UserRequestDto requestDto = UserPresets.userRequestDtoPreset();
        UserResponseDto responseDto = UserPresets.userResponseDto();

        doNothing().when(validationService).validateDate(request.getBirthday());
        doNothing().when(validationService).validateAge(request.getBirthday());

        when(userService.updateUser(userId, requestDto)).thenReturn(responseDto);

        MvcResult mvcResult = mockMvc.perform(put("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        UserResponse expectedResponse = objectMapper.readValue(contentAsString, UserResponse.class);

        verify(validationService).validateDate(request.getBirthday());
        verify(validationService).validateAge(request.getBirthday());
        verify(userService).updateUser(userId, requestDto);

        assertEquals(expectedResponse.getId(), userId);

        assertEquals(expectedResponse.getEmail(), request.getEmail());
        assertEquals(expectedResponse.getFirstName(), request.getFirstName());
        assertEquals(expectedResponse.getLastName(), request.getLastName());
        assertEquals(expectedResponse.getBirthday(), request.getBirthday());
        assertEquals(expectedResponse.getAddress(), request.getAddress());
        assertEquals(expectedResponse.getPhoneNumber(), request.getPhoneNumber());
    }

    @Test
    void testUpdateUserWhenNotFoundUserAndReturns404() throws Exception {
        Integer userId = 21;

        UserUpdateRequest request = UserPresets.userUpdateRequestPreset();
        UserRequestDto requestDto = UserPresets.userRequestDtoPreset();

        String exceptionMessage =  String.format("Can`t find user with id = %s", userId);

        doNothing().when(validationService).validateDate(request.getBirthday());
        doNothing().when(validationService).validateAge(request.getBirthday());

        when(userService.updateUser(userId, requestDto)).thenThrow(new UserNotFoundException(exceptionMessage));

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(exceptionMessage,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(validationService).validateDate(request.getBirthday());
        verify(validationService).validateAge(request.getBirthday());
        verify(userService).updateUser(userId, requestDto);
    }

    @Test
    void testUpdateUserWhenInvalidUserEmailPatternAndReturns400() throws Exception {
        Integer userId = 10;
        String invalidEmail = "example";
        String exceptionMessage = "Invalid email";

        UserRequest request = UserPresets.userRequestPreset();
        request.setEmail(invalidEmail);

        MvcResult mvcResult = mockMvc.perform(put("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                        result.getResolvedException()))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ExceptionDTO result = objectMapper.readValue(contentAsString, ExceptionDTO.class);

        assertEquals(result.getErrorMessage(), exceptionMessage);
        assertEquals(result.getErrorTitle(), "BAD_REQUEST");
        assertEquals(result.getErrorCode(), 400);
    }

    @Test
    void testUpdateUserWhenInvalidUserPhonePatternAndReturns400() throws Exception {
        Integer userId = 10;
        String invalidPhone = "56546546554";
        String exceptionMessage = "Invalid phone_number. Should be (xxx)xxx-xxxx or xxx-xxx-xxxx";

        UserRequest request = UserPresets.userRequestPreset();
        request.setPhoneNumber(invalidPhone);

        MvcResult mvcResult = mockMvc.perform(put("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                        result.getResolvedException()))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ExceptionDTO result = objectMapper.readValue(contentAsString, ExceptionDTO.class);

        assertEquals(result.getErrorMessage(), exceptionMessage);
        assertEquals(result.getErrorTitle(), "BAD_REQUEST");
        assertEquals(result.getErrorCode(), 400);
    }

    @Test
    void testDeleteUserAndReturns204() throws Exception {
        Integer userId = 21;

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent()).andReturn();

        verify(userService).deleteUser(userId);
    }

    @Test
    void testDeleteUserWhenNotFoundUserAndReturns404() throws Exception {
        Integer userId = 21;
        String exceptionMessage =  String.format("Can`t find user with id = %s", userId);

        doThrow(new UserNotFoundException(exceptionMessage)).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(exceptionMessage,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(userService).deleteUser(userId);
    }

    @Test
    void searchUsersByAge() throws Exception {
        LocalDate from = LocalDate.now().minusYears(19);
        LocalDate to = LocalDate.now().minusYears(18);
        List<UserResponseDto> usersResp = UserPresets.listOfUserResponseDtoPreset();

        when(userService.searchUser(from, to)).thenReturn(usersResp);

        MvcResult mvcResult = mockMvc.perform(get("/users?from={from}&to={to}", from, to))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        List<UserResponse> expectedResponse = objectMapper.readValue(contentAsString, new TypeReference<>() {});

        assertEquals(expectedResponse.size(), usersResp.size());
    }
}