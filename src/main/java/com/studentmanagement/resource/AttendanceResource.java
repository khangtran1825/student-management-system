package com.studentmanagement.resource;

import com.studentmanagement.dto.request.AttendanceRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.AttendanceResponse;
import com.studentmanagement.dto.response.ClassAttendanceResponse;
import com.studentmanagement.service.AttendanceService;
import java.time.LocalDate;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/attendances")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Attendance Management", description = "Quản lý điểm danh sinh viên")
public class AttendanceResource {

    @Inject
    AttendanceService service;

    @GET
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Lấy danh sách điểm danh", description = "Hỗ trợ lọc theo studentId và/hoặc scheduleId")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<AttendanceResponse>> getAll(
            @QueryParam("studentId") Long studentId,
            @QueryParam("scheduleId") Long scheduleId) {
        return ApiResponse.success(service.getAll(studentId, scheduleId));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Lấy thông tin điểm danh chi tiết theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy dữ liệu điểm danh")
    public ApiResponse<AttendanceResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @GET
    @Path("/class/{classId}/schedule/{scheduleId}/date/{date}")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Lấy danh sách điểm danh theo lớp")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ClassAttendanceResponse>> getClassAttendance(
            @PathParam("classId") Long classId,
            @PathParam("scheduleId") Long scheduleId,
            @PathParam("date") LocalDate date) {
        return ApiResponse.success(service.getClassAttendance(classId, scheduleId, date));
    }

    @POST
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Tạo mới điểm danh")
    @APIResponse(responseCode = "200", description = "Điểm danh thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<AttendanceResponse> create(@Valid AttendanceRequest request) {
        return ApiResponse.success("Điểm danh thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Cập nhật thông tin điểm danh")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy bản ghi")
    public ApiResponse<AttendanceResponse> update(@PathParam("id") Long id, @Valid AttendanceRequest request) {
        return ApiResponse.success("Cập nhật điểm danh thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Xóa điểm danh theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy bản ghi")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa điểm danh thành công", null);
    }
}
