package com.studentmanagement.dto.request;

import com.studentmanagement.entity.Student.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class StudentRequest {

    @NotBlank(message = "Student code is required")
    @Size(max = 20, message = "Student code must not exceed 20 characters")
    public String studentCode;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    public String fullName;

    @NotNull(message = "Gender is required")
    public Gender gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    public LocalDate dateOfBirth;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    public String email;

    @Size(max = 15, message = "Phone must not exceed 15 characters")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    public String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    public String address;

    @NotNull(message = "Class ID is required")
    public Long classId;

    @NotBlank(message = "Password is required for user account creation")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    public String password;

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    public String username; // Optional - if not provided, studentCode will be used as username
}
