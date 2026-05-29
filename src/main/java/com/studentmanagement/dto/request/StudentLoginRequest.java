package com.studentmanagement.dto.request;

import jakarta.validation.constraints.*;

public class StudentLoginRequest {
    @NotBlank(message = "Mã sinh viên hoặc username không được để trống")
    public String username; // Có thể là student code hoặc username

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    public String password;

    public StudentLoginRequest() {
    }

    public StudentLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
