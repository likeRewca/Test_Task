package com.task_cs.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserRequest {
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is mandatory field")
    private String email;

    @NotBlank(message = "First name is mandatory field")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name is mandatory field")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull(message = "Birthday is mandatory field")
    private LocalDate birthday;

    private String address;

    @Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}",
            message = "Invalid phone_number. Should be (xxx)xxx-xxxx or xxx-xxx-xxxx")
    @JsonProperty("phone_number")
    private String phoneNumber;
}
