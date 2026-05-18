// File: src/main/java/com/studentmanagement/resource/SubjectResource.java
package com.studentmanagement.resource;

import com.studentmanagement.dto.request.SubjectRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.SubjectResponse;
import com.studentmanagement.service.SubjectService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/subjects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
@Tag(name = "Subject Management", description = "Quản lý thông tin môn học (Chỉ dành cho ADMIN)")
public class SubjectResource {

    @Inject
    SubjectService subjectService;

    @GET
    @Operation(summary = "Lấy danh sách tất cả môn học")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<SubjectResponse>> getAll() {
        return ApiResponse.success(subjectService.getAll());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Lấy thông tin môn học theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<SubjectResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(subjectService.getById(id));
    }

    @POST
    @Operation(summary = "Tạo mới một môn học")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    public ApiResponse<SubjectResponse> create(@Valid SubjectRequest request) {
        return ApiResponse.success("Tạo môn học thành công", subjectService.create(request));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Cập nhật thông tin môn học")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    public ApiResponse<SubjectResponse> update(@PathParam("id") Long id, @Valid SubjectRequest request) {
        return ApiResponse.success("Cập nhật môn học thành công", subjectService.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Xóa môn học theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        subjectService.delete(id);
        return ApiResponse.success("Xóa môn học thành công", null);
    }
}