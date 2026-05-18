package com.studentmanagement.dto.response;

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
}
