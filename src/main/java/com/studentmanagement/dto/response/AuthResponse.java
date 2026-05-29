package com.studentmanagement.dto.response;

public class AuthResponse {
    public String token;
    public String tokenType = "Bearer";
    public Long expiresIn;
    public String username;
    public String role;
    public Long studentId;
    public Boolean mustChangePassword;

    public AuthResponse() {}

    public AuthResponse(String token, Long expiresIn, String username, String role, Boolean mustChangePassword) {
        this(token, expiresIn, username, role, null, mustChangePassword);
    }

    public AuthResponse(String token, Long expiresIn, String username, String role, Long studentId, Boolean mustChangePassword) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
        this.role = role;
        this.studentId = studentId;
        this.mustChangePassword = mustChangePassword;
    }
}
