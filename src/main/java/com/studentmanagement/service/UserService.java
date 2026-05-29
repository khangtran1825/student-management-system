package com.studentmanagement.service;

import com.studentmanagement.dto.request.UserRequest;
import com.studentmanagement.dto.request.UserUpdateRequest;
import com.studentmanagement.dto.response.UserResponse;
import com.studentmanagement.entity.User;
import com.studentmanagement.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public List<UserResponse> getAll() {
        return userRepository.listAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public com.studentmanagement.dto.response.PagedResponse<UserResponse> getAll(int page, int size, String search, String role) {
        // Normalize inputs
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        // Build query dynamically
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasRole = role != null && !role.trim().isEmpty();

        io.quarkus.hibernate.orm.panache.PanacheQuery<User> pq;
        if (hasSearch && hasRole) {
            String q = "(username like ?1 or email like ?1) and role = ?2";
            pq = userRepository.find(q, "%" + search.trim() + "%", role.toUpperCase());
        } else if (hasSearch) {
            String q = "(username like ?1 or email like ?1)";
            pq = userRepository.find(q, "%" + search.trim() + "%");
        } else if (hasRole) {
            String q = "role = ?1";
            pq = userRepository.find(q, role.toUpperCase());
        } else {
            pq = userRepository.findAll();
        }

        // Apply pagination
        pq.page(io.quarkus.panache.common.Page.of(page, size));
        long total = pq.count();
        java.util.List<UserResponse> items = pq.list().stream().map(this::toResponse).collect(Collectors.toList());

        return new com.studentmanagement.dto.response.PagedResponse<>(items, total, page, size);
    }

    public List<UserResponse> getByRole(String role) {
        return userRepository.find("role", role).list().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
        }
        return toResponse(user);
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        // Check username already exists
        if (userRepository.find("username", request.username).firstResultOptional().isPresent()) {
            throw new RuntimeException("Username đã tồn tại: " + request.username);
        }

        // Check email already exists
        if (userRepository.find("email", request.email).firstResultOptional().isPresent()) {
            throw new RuntimeException("Email đã tồn tại: " + request.email);
        }

        User user = new User();
        user.username = request.username;
        user.password = BcryptUtil.bcryptHash(request.password);
        user.email = request.email;
        user.role = request.role.toUpperCase();
        user.active = true;

        userRepository.persistAndFlush(user);
        return toResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
        }

        // Check email uniqueness if changed
        if (!user.email.equals(request.email) &&
                userRepository.find("email", request.email).firstResultOptional().isPresent()) {
            throw new RuntimeException("Email đã tồn tại: " + request.email);
        }

        user.email = request.email;
        user.role = request.role.toUpperCase();
        if (request.active != null) {
            user.active = request.active;
        }
        
        userRepository.persistAndFlush(user);

        return toResponse(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
        }
        userRepository.delete(user);
    }

    @Transactional
    public void resetPassword(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
        }
        user.password = BcryptUtil.bcryptHash("123456");
        user.mustChangePassword = true;
        userRepository.persistAndFlush(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.id,
                user.username,
                user.email,
                user.role,
                user.active,
                user.mustChangePassword,
                user.createdAt,
                user.updatedAt
        );
    }
}
