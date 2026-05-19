package com.studentmanagement.resource;

import com.studentmanagement.dto.request.SemesterRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.SemesterResponse;
import com.studentmanagement.service.SemesterService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/semesters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Semester Management", description = "Quản lý thông tin học kỳ")
public class SemesterResource {

    @Inject
    SemesterService service;

    @GET
    @Operation(summary = "Lấy danh sách tất cả học kỳ")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<SemesterResponse>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết học kỳ theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy học kỳ")
    public ApiResponse<SemesterResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @Operation(summary = "Tạo mới một học kỳ")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<SemesterResponse> create(@Valid SemesterRequest request) {
        return ApiResponse.success("Tạo học kỳ thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Cập nhật thông tin học kỳ")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy học kỳ")
    public ApiResponse<SemesterResponse> update(@PathParam("id") Long id, @Valid SemesterRequest request) {
        return ApiResponse.success("Cập nhật học kỳ thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Xóa học kỳ theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy học kỳ")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa học kỳ thành công", null);
    }
}
