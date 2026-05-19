package com.studentmanagement.resource;

import com.studentmanagement.dto.request.AcademicYearRequest;
import com.studentmanagement.dto.response.AcademicYearResponse;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.service.AcademicYearService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/academic-years")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Academic Year Management", description = "Quản lý thông tin năm học")
public class AcademicYearResource {

    @Inject
    AcademicYearService service;

    @GET
    @Operation(summary = "Lấy danh sách tất cả năm học")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<AcademicYearResponse>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết năm học theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy năm học")
    public ApiResponse<AcademicYearResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @Operation(summary = "Tạo mới một năm học")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<AcademicYearResponse> create(@Valid AcademicYearRequest request) {
        return ApiResponse.success("Tạo năm học thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Cập nhật thông tin năm học")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy năm học")
    public ApiResponse<AcademicYearResponse> update(@PathParam("id") Long id, @Valid AcademicYearRequest request) {
        return ApiResponse.success("Cập nhật năm học thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Xóa năm học theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy năm học")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa năm học thành công", null);
    }
}
