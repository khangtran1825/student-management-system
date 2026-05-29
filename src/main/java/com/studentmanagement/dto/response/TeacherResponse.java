package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Teacher;
import java.time.LocalDate;

public class TeacherResponse {
    public Long id;
    public String teacherCode;
    public String fullName;
    public String gender;
    public LocalDate dateOfBirth;
    public String email;
    public String phone;
    public String department;

    public TeacherResponse(Teacher entity) {
        this.id = entity.id;
        this.teacherCode = entity.teacherCode;
        this.fullName = entity.fullName;
        this.gender = entity.gender != null ? entity.gender.name() : null;
        this.dateOfBirth = entity.dateOfBirth;
        this.email = entity.email;
        this.phone = entity.phone;
        this.department = entity.department;
    }
}
