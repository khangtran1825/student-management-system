package com.studentmanagement.dto.response;

import com.studentmanagement.entity.AcademicYear;
import java.time.LocalDate;

public class AcademicYearResponse {
    public Long id;
    public String name;
    public LocalDate startDate;
    public LocalDate endDate;

    public AcademicYearResponse(AcademicYear entity) {
        this.id = entity.id;
        this.name = entity.name;
        this.startDate = entity.startDate;
        this.endDate = entity.endDate;
    }
}
