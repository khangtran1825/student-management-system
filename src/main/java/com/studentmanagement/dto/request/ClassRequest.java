package com.studentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClassRequest {

    @NotBlank(message = "Class code is required")
    @Size(max = 20, message = "Class code must not exceed 20 characters")
    public String classCode;

    @NotBlank(message = "Class name is required")
    @Size(max = 100, message = "Class name must not exceed 100 characters")
    public String className;

    @NotBlank(message = "Major is required")
    @Size(max = 100, message = "Major must not exceed 100 characters")
    public String major;
}
