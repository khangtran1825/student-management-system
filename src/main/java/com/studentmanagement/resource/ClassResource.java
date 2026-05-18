// File: src/main/java/com/studentmanagement/resource/ClassResource.java
package com.studentmanagement.resource;

import com.studentmanagement.dto.request.ClassRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.ClassResponse;
import com.studentmanagement.service.ClassService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/classes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
@Tag(name = "Class Management", description = "Quản lý thông tin lớp học (Chỉ dành cho ADMIN)")
public class ClassResource {

    @Inject
    ClassService classService;

    @GET
    @Operation(summary = "Lấy danh sách tất cả lớp học")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ClassResponse>> getAll() {
        return ApiResponse.success(classService.getAll());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết lớp học theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy ID")
    public ApiResponse<ClassResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(classService.getById(id));
    }

    @POST
    @Operation(summary = "Tạo mới một lớp học")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    public ApiResponse<ClassResponse> create(@Valid ClassRequest request) {
        return ApiResponse.success("Tạo lớp học thành công", classService.create(request));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Cập nhật thông tin lớp học")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    public ApiResponse<ClassResponse> update(@PathParam("id") Long id, @Valid ClassRequest request) {
        return ApiResponse.success("Cập nhật lớp học thành công", classService.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Xóa lớp học theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        classService.delete(id);
        return ApiResponse.success("Xóa lớp học thành công", null);
    }
}