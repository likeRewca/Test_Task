package com.task_cs.api.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class UserResponseDto {
    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private String address;

    private String phoneNumber;
}
