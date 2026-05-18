package com.studentmanagement.dto.request;

import jakarta.validation.constraints.*;

public class SubjectRequest {

    @NotBlank(message = "Subject code is required")
    @Size(max = 20, message = "Subject code must not exceed 20 characters")
    public String subjectCode;

    @NotBlank(message = "Subject name is required")
    @Size(max = 150, message = "Subject name must not exceed 150 characters")
    public String subjectName;

    @NotNull(message = "Credits is required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 10, message = "Credits must not exceed 10")
    public Integer credits;
}

