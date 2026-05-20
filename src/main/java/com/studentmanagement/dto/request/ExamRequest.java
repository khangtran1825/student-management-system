package com.studentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ExamRequest {
    @NotNull
    public Long subjectId;
    @NotNull
    public Long semesterId;
    @NotNull
    public LocalDateTime examDate;
    @NotBlank
    public String room;
}
