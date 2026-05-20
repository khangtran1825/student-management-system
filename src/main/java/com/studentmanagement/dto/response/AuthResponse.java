package com.studentmanagement.dto.response;

public class AuthResponse {
    public String token;
    public String tokenType = "Bearer";
    public Long expiresIn;
    public String username;
    public String role;
    public Long studentId;

    public AuthResponse() {}

    public AuthResponse(String token, Long expiresIn, String username, String role) {
        this(token, expiresIn, username, role, null);
    }

    public AuthResponse(String token, Long expiresIn, String username, String role, Long studentId) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
        this.role = role;
        this.studentId = studentId;
    }
}
