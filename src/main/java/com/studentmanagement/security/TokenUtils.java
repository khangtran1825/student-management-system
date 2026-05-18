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
        Set<String> roles = new HashSet<>();
        roles.add(role);

        return Jwt.issuer(issuer)
                .upn(username)
                .groups(roles)
                .expiresIn(Duration.ofSeconds(expiry))
                .sign();
    }

    public Long getExpiry() {
        return expiry;
    }
}