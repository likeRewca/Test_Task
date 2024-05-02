package com.task_cs.api.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class UserRequestDto {

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private String address;

    private String phoneNumber;

}
