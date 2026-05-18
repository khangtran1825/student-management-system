// File: src/main/java/com/studentmanagement/service/AuthService.java
package com.studentmanagement.service;

import com.studentmanagement.dto.request.LoginRequest;
import com.studentmanagement.dto.request.RegisterRequest;
import com.studentmanagement.dto.response.AuthResponse;
import com.studentmanagement.entity.Student;
import com.studentmanagement.entity.User;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.repository.UserRepository;
import com.studentmanagement.security.TokenUtils;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    StudentRepository studentRepository;

    @Inject
    TokenUtils tokenUtils;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.find("username", request.username).firstResultOptional()
                .orElseThrow(() -> new BusinessException("Sai thông tin đăng nhập"));

        if (!BcryptUtil.matches(request.password, user.password)) {
            throw new BusinessException("Sai thông tin đăng nhập");
        }

        if (!user.active) {
            throw new BusinessException("Tài khoản đã bị khóa");
        }

        String token = tokenUtils.generateToken(user.username, user.role.name());

        return new AuthResponse(token, tokenUtils.getExpiry(), user.username, user.role.name());
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.find("username", request.username).firstResultOptional().isPresent()) {
            throw new BusinessException("Username đã tồn tại");
        }

        User user = new User();
        user.username = request.username;
        user.password = BcryptUtil.bcryptHash(request.password);
        user.role = request.role;
        user.active = true;

        if (request.studentId != null) {
            Student student = studentRepository.findById(request.studentId);
            if (student == null) {
                throw new BusinessException("Không tìm thấy sinh viên");
            }
            user.student = student;
        }

        userRepository.persist(user);
    }
}