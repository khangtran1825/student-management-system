package com.studentmanagement.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ScoreRequest {

    @NotNull(message = "Student ID is required")
    public Long studentId;

    @NotNull(message = "Subject ID is required")
    public Long subjectId;

    @NotNull(message = "Midterm score is required")
    @DecimalMin(value = "0.0", message = "Midterm score must be at least 0")
    @DecimalMax(value = "10.0", message = "Midterm score must not exceed 10")
    public BigDecimal midtermScore;

    @NotNull(message = "Final score is required")
    @DecimalMin(value = "0.0", message = "Final score must be at least 0")
    @DecimalMax(value = "10.0", message = "Final score must not exceed 10")
    public BigDecimal finalScore;
}