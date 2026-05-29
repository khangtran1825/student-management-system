package com.studentmanagement.dto.response;

import java.time.LocalDateTime;

public class UserResponse {
    public Long id;
    public String username;
    public String email;
    public String role;
    public Long studentId;
    public Boolean active;
    public Boolean mustChangePassword;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String email, String role, Boolean active, Boolean mustChangePassword, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.active = active;
        this.mustChangePassword = mustChangePassword;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
