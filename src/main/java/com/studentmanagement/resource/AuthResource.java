// File: src/main/java/com/studentmanagement/resource/AuthResource.java
package com.studentmanagement.resource;

import com.studentmanagement.dto.request.LoginRequest;
import com.studentmanagement.dto.request.RegisterRequest;
import com.studentmanagement.dto.request.ChangePasswordRequest;
import com.studentmanagement.dto.request.StudentLoginRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.AuthResponse;
import com.studentmanagement.service.AuthService;
import com.studentmanagement.service.StudentAuthService;
import com.studentmanagement.dto.response.UserResponse;
import com.studentmanagement.repository.UserRepository;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
@Tag(name = "Authentication", description = "APIs phục vụ đăng nhập và đăng ký tài khoản")
public class AuthResource {

    @Inject
    AuthService authService;

    @Inject
    StudentAuthService studentAuthService;

    @Inject
    UserRepository userRepository;

    @POST
    @Path("/login")
    @Operation(summary = "Đăng nhập hệ thống", description = "Trả về JWT Token nếu thông tin chính xác")
    @APIResponse(responseCode = "200", description = "Đăng nhập thành công")
    @APIResponse(responseCode = "400", description = "Sai thông tin đăng nhập")
    public ApiResponse<AuthResponse> login(@Valid LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ApiResponse.success("Đăng nhập thành công", response);
    }

    @POST
    @Path("/student-login")
    @Operation(summary = "Đăng nhập dành cho sinh viên", description = "Đăng nhập bằng mã sinh viên/username và mật khẩu")
    @APIResponse(responseCode = "200", description = "Đăng nhập thành công")
    @APIResponse(responseCode = "400", description = "Sai thông tin đăng nhập")
    public ApiResponse<AuthResponse> studentLogin(@Valid StudentLoginRequest request) {
        AuthResponse response = studentAuthService.loginStudent(request);
        return ApiResponse.success("Đăng nhập thành công", response);
    }

    @POST
    @Path("/register")
    @Operation(summary = "Đăng ký tài khoản mới", description = "Tạo tài khoản người dùng mới trong hệ thống")
    @APIResponse(responseCode = "200", description = "Đăng ký thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc username trùng lặp")
    public ApiResponse<Void> register(@Valid RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("Đăng ký tài khoản thành công", null);
    }

    @GET
    @Path("/me")
    public ApiResponse<UserResponse> me(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal() != null ? sec.getUserPrincipal().getName() : null;
        if (username == null) {
            return ApiResponse.success(null);
        }
        com.studentmanagement.entity.User user = userRepository.find("username", username).firstResult();
        if (user == null) return ApiResponse.success(null);
        UserResponse resp = new UserResponse();
        resp.id = user.id;
        resp.username = user.username;
        resp.email = user.email;
        resp.role = user.role;
        resp.studentId = user.student != null ? user.student.id : null;
        resp.active = user.active;
        resp.mustChangePassword = user.mustChangePassword;
        return ApiResponse.success(resp);
    }

    @POST
    @Path("/change-password")
    @Operation(summary = "Đổi mật khẩu")
    @APIResponse(responseCode = "200", description = "Đổi mật khẩu thành công")
    @APIResponse(responseCode = "400", description = "Sai mật khẩu hiện tại")
    public ApiResponse<Void> changePassword(@Context SecurityContext sec, @Valid ChangePasswordRequest request) {
        String username = sec.getUserPrincipal().getName();
        authService.changePassword(username, request);
        return ApiResponse.success("Đổi mật khẩu thành công", null);
    }
}