package com.task_cs.api.model.dto.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDTO {
    private Integer errorCode;

    private String errorTitle;

    private String errorMessage;

    private LocalDateTime errorTimestamp;
}