package com.studentmanagement.resource;

import com.studentmanagement.dto.request.TeacherRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.PagedResponse;
import com.studentmanagement.dto.response.TeacherResponse;
import com.studentmanagement.service.TeacherService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/teachers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Teacher Management", description = "Quản lý hồ sơ giảng viên")
public class TeacherResource {

    @Inject
    TeacherService service;

    @GET
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Lấy danh sách giảng viên (có phân trang và tìm kiếm)")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<PagedResponse<TeacherResponse>> getAll(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size,
            @QueryParam("search") String search
    ) {
        return ApiResponse.success(service.getAll(page, size, search));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Lấy chi tiết giảng viên theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy")
    public ApiResponse<TeacherResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Thêm mới giảng viên")
    @APIResponse(responseCode = "200", description = "Thêm thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<TeacherResponse> create(@Valid TeacherRequest request) {
        return ApiResponse.success("Thêm giảng viên thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Cập nhật giảng viên")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy")
    public ApiResponse<TeacherResponse> update(@PathParam("id") Long id, @Valid TeacherRequest request) {
        return ApiResponse.success("Cập nhật giảng viên thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Xóa giảng viên")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa giảng viên thành công", null);
    }
}
