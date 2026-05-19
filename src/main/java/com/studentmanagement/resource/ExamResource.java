package com.studentmanagement.resource;

import com.studentmanagement.dto.request.ExamRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.ExamResponse;
import com.studentmanagement.service.ExamService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/exams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Exam Management", description = "Quản lý thông tin lịch thi")
public class ExamResource {

    @Inject
    ExamService service;

    @GET
    @Operation(summary = "Lấy danh sách tất cả lịch thi")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ExamResponse>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết lịch thi theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch thi")
    public ApiResponse<ExamResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @Operation(summary = "Tạo mới một lịch thi")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<ExamResponse> create(@Valid ExamRequest request) {
        return ApiResponse.success("Tạo lịch thi thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Cập nhật thông tin lịch thi")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch thi")
    public ApiResponse<ExamResponse> update(@PathParam("id") Long id, @Valid ExamRequest request) {
        return ApiResponse.success("Cập nhật lịch thi thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Xóa lịch thi theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch thi")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa lịch thi thành công", null);
    }
}
