package com.studentmanagement.resource;

import com.studentmanagement.dto.request.UserRequest;
import com.studentmanagement.dto.request.UserUpdateRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.UserResponse;
import com.studentmanagement.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User Management", description = "Quản lý người dùng và tài khoản")
public class UserResource {

    @Inject
    UserService service;

    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Lấy danh sách tất cả người dùng (hỗ trợ pagination & lọc)")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<com.studentmanagement.dto.response.PagedResponse<UserResponse>> getAll(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size,
            @QueryParam("search") String search,
            @QueryParam("role") String role
    ) {
        return ApiResponse.success(service.getAll(page, size, search, role));
    }

    @GET
    @Path("/role/{role}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Lấy danh sách người dùng theo role")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<UserResponse>> getByRole(@PathParam("role") String role) {
        return ApiResponse.success(service.getByRole(role));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Lấy thông tin chi tiết người dùng theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    public ApiResponse<UserResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Tạo mới một người dùng")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc người dùng đã tồn tại")
    public ApiResponse<UserResponse> create(@Valid UserRequest request) {
        return ApiResponse.success("Tạo người dùng thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Cập nhật thông tin người dùng")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    public ApiResponse<UserResponse> update(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        return ApiResponse.success("Cập nhật người dùng thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Xóa người dùng theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa người dùng thành công", null);
    }

    @POST
    @Path("/{id}/reset-password")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Reset mật khẩu người dùng về mặc định (123456)")
    @APIResponse(responseCode = "200", description = "Reset mật khẩu thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    public ApiResponse<Void> resetPassword(@PathParam("id") Long id) {
        service.resetPassword(id);
        return ApiResponse.success("Reset mật khẩu thành công", null);
    }
}
