package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Semester;
import java.time.LocalDate;

public class SemesterResponse {
    public Long id;
    public String name;
    public AcademicYearResponse academicYear;
    public LocalDate startDate;
    public LocalDate endDate;

    public SemesterResponse(Semester entity) {
        this.id = entity.id;
        this.name = entity.name;
        if (entity.academicYear != null) {
            this.academicYear = new AcademicYearResponse(entity.academicYear);
        }
        this.startDate = entity.startDate;
        this.endDate = entity.endDate;
    }
}
