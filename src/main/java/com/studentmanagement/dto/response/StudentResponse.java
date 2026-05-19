package com.studentmanagement.dto.response;

import com.studentmanagement.entity.ClassEntity;
import com.studentmanagement.entity.Student;
import com.studentmanagement.entity.Student.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentResponse {
    public Long id;
    public String studentCode;
    public String fullName;
    public Gender gender;
    public LocalDate dateOfBirth;
    public String email;
    public String phone;
    public String address;
    public Long classId;
    public String classCode;
    public String className;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public StudentResponse() {
    }

    public StudentResponse(Student entity) {
        this.id = entity.id;
        this.studentCode = entity.studentCode;
        this.fullName = entity.fullName;
        this.gender = entity.gender;
        this.dateOfBirth = entity.dateOfBirth;
        this.email = entity.email;
        this.phone = entity.phone;
        this.address = entity.address;
        this.createdAt = entity.createdAt;
        this.updatedAt = entity.updatedAt;

        ClassEntity classEntity = entity.classEntity;
        if (classEntity != null) {
            this.classId = classEntity.id;
            this.classCode = classEntity.classCode;
            this.className = classEntity.className;
        }
    }
}
