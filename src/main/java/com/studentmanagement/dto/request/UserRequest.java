package com.studentmanagement.dto.request;

import jakarta.validation.constraints.*;

public class UserRequest {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải có từ 3 đến 50 ký tự")
    public String username;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    public String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    public String email;

    @NotBlank(message = "Role không được để trống")
    @Pattern(regexp = "ADMIN|TEACHER|STUDENT", message = "Role phải là ADMIN, TEACHER hoặc STUDENT")
    public String role;

    public UserRequest() {
    }

    public UserRequest(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
