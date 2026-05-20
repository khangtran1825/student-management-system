package com.studentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AcademicYearRequest {
    @NotBlank
    public String name;
    @NotNull
    public LocalDate startDate;
    @NotNull
    public LocalDate endDate;
}
