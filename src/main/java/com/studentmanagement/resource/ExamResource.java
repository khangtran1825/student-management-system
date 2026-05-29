package com.studentmanagement.resource;

import com.studentmanagement.dto.request.ExamRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.ExamResponse;
import com.studentmanagement.entity.User;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.repository.UserRepository;
import com.studentmanagement.service.ExamService;
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

@Path("/api/exams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Exam Management", description = "Quản lý thông tin lịch thi")
public class ExamResource {

    @Inject
    ExamService service;

    @Inject
    UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Lấy danh sách tất cả lịch thi")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ExamResponse>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GET
    @Path("/me")
    @RolesAllowed({"TEACHER", "STUDENT"})
    @Operation(summary = "Lấy lịch thi phù hợp với người dùng hiện tại")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ExamResponse>> getMyExams(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal() != null ? sec.getUserPrincipal().getName() : null;
        if (username == null) {
            throw new BusinessException("Không xác định được người dùng hiện tại");
        }

        if (sec.isUserInRole("TEACHER")) {
            User user = userRepository.find("username", username).firstResult();
            if (user == null || user.teacher == null) {
                throw new BusinessException("Không tìm thấy tài khoản hoặc hồ sơ giảng viên");
            }
            return ApiResponse.success(service.getByTeacherId(user.teacher.id));
        }

        User user = userRepository.find("username", username).firstResult();
        if (user == null || user.student == null || user.student.classEntity == null) {
            throw new BusinessException("Không tìm thấy lớp học của sinh viên");
        }

        // Student: keep current behavior simple by returning all exams; frontend will filter if needed later.
        return ApiResponse.success(service.getAll());
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Lấy thông tin chi tiết lịch thi theo ID")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch thi")
    public ApiResponse<ExamResponse> getById(@PathParam("id") Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Tạo mới một lịch thi")
    @APIResponse(responseCode = "200", description = "Tạo mới thành công")
    @APIResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    public ApiResponse<ExamResponse> create(@Valid ExamRequest request) {
        return ApiResponse.success("Tạo lịch thi thành công", service.create(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Cập nhật thông tin lịch thi")
    @APIResponse(responseCode = "200", description = "Cập nhật thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch thi")
    public ApiResponse<ExamResponse> update(@PathParam("id") Long id, @Valid ExamRequest request) {
        return ApiResponse.success("Cập nhật lịch thi thành công", service.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Xóa lịch thi theo ID")
    @APIResponse(responseCode = "200", description = "Xóa thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy lịch thi")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa lịch thi thành công", null);
    }
}
