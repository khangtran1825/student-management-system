// File: src/main/java/com/studentmanagement/security/TokenUtils.java
package com.studentmanagement.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class TokenUtils {

    @ConfigProperty(name = "app.jwt.issuer", defaultValue = "student-management-api")
    String issuer;

    @ConfigProperty(name = "app.jwt.expiry", defaultValue = "86400")
    Long expiry;

    public String generateToken(String username, String role) {
        return generateToken(username, role, null);
    }

    public String generateToken(String username, String role, Long studentId) {
        Set<String> roles = new HashSet<>();
        roles.add(role);

        var builder = Jwt.issuer(issuer)
            .upn(username)
            .groups(roles)
            .expiresIn(Duration.ofSeconds(expiry));

        if (studentId != null) {
            builder.claim("studentId", studentId);
        }

        return builder.sign();
    }

    public Long getExpiry() {
        return expiry;
    }
}