// File: src/main/java/com/studentmanagement/resource/AuthResource.java
package com.studentmanagement.resource;

import com.studentmanagement.dto.request.LoginRequest;
import com.studentmanagement.dto.request.RegisterRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.AuthResponse;
import com.studentmanagement.service.AuthService;
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
    @Path("/register")
    @Operation(summary = "Đăng ký tài khoản mới", description = "Tạo tài khoản người dùng mới trong hệ thống")
    @APIResponse(responseCode = "200", description = "Đăng ký thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc username trùng lặp")
    public ApiResponse<Void> register(@Valid RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("Đăng ký tài khoản thành công", null);
    }
}