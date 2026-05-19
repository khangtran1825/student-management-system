package com.studentmanagement.resource;

import com.studentmanagement.dto.request.ScheduleRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.ScheduleResponse;
import com.studentmanagement.service.ScheduleService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/schedules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Schedule Management", description = "Quản lý thông tin lịch học")
public class ScheduleResource {

    @Inject
    ScheduleService service;

    @GET
    @Operation(summary = "Lấy danh sách tất cả lịch học")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ScheduleResponse>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết lịch học theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch học")
    public ApiResponse<ScheduleResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @Operation(summary = "Tạo mới một lịch học")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<ScheduleResponse> create(@Valid ScheduleRequest request) {
        return ApiResponse.success("Tạo lịch học thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Cập nhật thông tin lịch học")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch học")
    public ApiResponse<ScheduleResponse> update(@PathParam("id") Long id, @Valid ScheduleRequest request) {
        return ApiResponse.success("Cập nhật lịch học thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Xóa lịch học theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch học")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa lịch học thành công", null);
    }
}
