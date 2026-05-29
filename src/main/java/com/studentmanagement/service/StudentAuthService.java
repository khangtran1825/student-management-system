package com.studentmanagement.service;

import com.studentmanagement.dto.request.StudentLoginRequest;
import com.studentmanagement.dto.response.AuthResponse;
import com.studentmanagement.entity.Student;
import com.studentmanagement.repository.StudentRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

@ApplicationScoped
public class StudentAuthService {

    private static final Logger LOG = Logger.getLogger(StudentAuthService.class);

    @Inject
    StudentRepository studentRepository;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    private static final long TOKEN_EXPIRATION_SECONDS = 86400; // 24 hours

    public AuthResponse loginStudent(StudentLoginRequest request) {
        LOG.info("Student login attempt: " + request.username);

        // Find student by username or student code
        Optional<Student> studentOpt = studentRepository.find("username", request.username).firstResultOptional();
        
        if (studentOpt.isEmpty()) {
            // Try searching by student code
            studentOpt = studentRepository.find("studentCode", request.username).firstResultOptional();
        }

        if (studentOpt.isEmpty()) {
            LOG.warn("Student not found: " + request.username);
            throw new RuntimeException("Tên đăng nhập hoặc mã sinh viên không tồn tại");
        }

        Student student = studentOpt.get();

        // Verify password
        if (student.password == null || !BcryptUtil.matches(request.password, student.password)) {
            LOG.warn("Invalid password for student: " + request.username);
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        // Generate JWT token
        Instant iat = Instant.now();
        Instant exp = iat.plusSeconds(TOKEN_EXPIRATION_SECONDS);

        String token = Jwt.issuer(issuer)
                .subject(student.id.toString())
                .claim("upn", student.username != null ? student.username : student.studentCode)
                .claim("username", student.username != null ? student.username : student.studentCode)
                .claim("role", "STUDENT")
                .claim("studentId", student.id)
                .claim("groups", new HashSet<String>() {{ add("STUDENT"); }})
                .issuedAt(iat)
                .expiresAt(exp)
                .sign();

        LOG.info("Student login successful: " + student.username);
        return new AuthResponse(token, TOKEN_EXPIRATION_SECONDS, student.username != null ? student.username : student.studentCode, "STUDENT", student.id, false);
    }
}
