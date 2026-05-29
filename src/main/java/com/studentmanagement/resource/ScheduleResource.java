package com.studentmanagement.resource;

import com.studentmanagement.dto.request.ScheduleRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.ScheduleResponse;
import com.studentmanagement.entity.User;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.repository.UserRepository;
import com.studentmanagement.service.ScheduleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
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

    @Inject
    UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Lấy danh sách tất cả lịch học")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ScheduleResponse>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GET
    @Path("/me")
    @RolesAllowed({"TEACHER", "STUDENT"})
    @Operation(summary = "Lấy lịch học / lịch giảng dạy của người dùng hiện tại")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ScheduleResponse>> getMySchedule(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal() != null ? sec.getUserPrincipal().getName() : null;
        if (username == null) {
            throw new BusinessException("Không xác định được người dùng hiện tại");
        }

        User user = userRepository.find("username", username).firstResult();
        if (user == null) {
            throw new BusinessException("Không tìm thấy tài khoản hiện tại");
        }

        if (sec.isUserInRole("TEACHER")) {
            if (user.teacher == null) {
                throw new BusinessException("Không tìm thấy hồ sơ giảng viên");
            }
            return ApiResponse.success(service.getByTeacherId(user.teacher.id));
        }

        if (user == null || user.student == null || user.student.classEntity == null) {
            throw new BusinessException("Không tìm thấy lớp học của sinh viên");
        }

        return ApiResponse.success(service.getByClassId(user.student.classEntity.id));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Lấy thông tin chi tiết lịch học theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch học")
    public ApiResponse<ScheduleResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Tạo mới một lịch học")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<ScheduleResponse> create(@Valid ScheduleRequest request) {
        return ApiResponse.success("Tạo lịch học thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Cập nhật thông tin lịch học")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch học")
    public ApiResponse<ScheduleResponse> update(@PathParam("id") Long id, @Valid ScheduleRequest request) {
        return ApiResponse.success("Cập nhật lịch học thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Xóa lịch học theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch học")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa lịch học thành công", null);
    }
}
