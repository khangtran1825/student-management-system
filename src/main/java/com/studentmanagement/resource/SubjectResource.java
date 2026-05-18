package com.studentmanagement.resource;

import com.studentmanagement.dto.request.SubjectRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.PageResponse;
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
@Tag(name = "Subject Management", description = "Quản lý thông tin môn học")
public class SubjectResource {

    @Inject
    SubjectService subjectService;

    @GET
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Lấy danh sách tất cả môn học")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<SubjectResponse>> getAll() {
        return ApiResponse.success(subjectService.getAll());
    }

    @GET
    @Path("/search")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Tìm kiếm và phân trang môn học")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<PageResponse<SubjectResponse>> search(
            @QueryParam("keyword") String keyword,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return ApiResponse.success(subjectService.search(keyword, page, size));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Lấy thông tin môn học theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy")
    public ApiResponse<SubjectResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(subjectService.getById(id));
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Tạo mới một môn học")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Mã môn học đã tồn tại")
    public ApiResponse<SubjectResponse> create(@Valid SubjectRequest request) {
        return ApiResponse.success("Tạo môn học thành công", subjectService.create(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Cập nhật thông tin môn học")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    public ApiResponse<SubjectResponse> update(@PathParam("id") Long id, @Valid SubjectRequest request) {
        return ApiResponse.success("Cập nhật môn học thành công", subjectService.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Xóa môn học theo ID (chỉ khi không có điểm số liên quan)")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "400", description = "Môn học vẫn còn điểm số liên quan")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        subjectService.delete(id);
        return ApiResponse.success("Xóa môn học thành công", null);
    }
}