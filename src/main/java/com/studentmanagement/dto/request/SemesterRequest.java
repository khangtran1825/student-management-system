package com.studentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SemesterRequest {
    @NotBlank
    public String name;
    @NotNull
    public Long academicYearId;
    @NotNull
    public LocalDate startDate;
    @NotNull
    public LocalDate endDate;
}
