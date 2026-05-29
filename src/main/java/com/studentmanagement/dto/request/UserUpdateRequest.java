package com.studentmanagement.dto.request;

import jakarta.validation.constraints.*;

public class UserUpdateRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    public String email;

    @NotBlank(message = "Role không được để trống")
    @Pattern(regexp = "ADMIN|TEACHER|STUDENT", message = "Role phải là ADMIN, TEACHER hoặc STUDENT")
    public String role;
    
    public Boolean active;
}
