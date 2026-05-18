// File: src/main/java/com/studentmanagement/resource/ScoreResource.java
package com.studentmanagement.resource;

import com.studentmanagement.dto.request.ScoreRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.ScoreResponse;
import com.studentmanagement.entity.User;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.repository.UserRepository;
import com.studentmanagement.service.ScoreService; // Giả định đã có ScoreService xử lý nghiệp vụ điểm số
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/scores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Score Management", description = "Quản lý điểm số của sinh viên")
public class ScoreResource {

    @Inject
    ScoreService scoreService;

    @Inject
    UserRepository userRepository;

    @GET
    @Path("/student/{studentId}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Xem điểm số theo mã ID sinh viên", description = "Sinh viên chỉ được truyền đúng ID của chính mình")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<List<ScoreResponse>> getScoresByStudent(@PathParam("studentId") Long studentId, @Context SecurityContext sec) {
        if (sec.isUserInRole("STUDENT")) {
            String username = sec.getUserPrincipal().getName();
            User user = userRepository.find("username", username).firstResult();
            if (user == null || user.student == null || !user.student.id.equals(studentId)) {
                throw new BusinessException("Bạn không có quyền xem điểm của sinh viên khác.");
            }
        }
        return ApiResponse.success(scoreService.getScoresByStudentId(studentId));
    }

    @POST
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Nhập điểm môn học cho sinh viên")
    @APIResponse(responseCode = "200", description = "Nhập điểm thành công")
    public ApiResponse<ScoreResponse> enterScore(@Valid ScoreRequest request) {
        return ApiResponse.success("Nhập điểm thành công", scoreService.createScore(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Sửa đổi điểm số đã nhập")
    @APIResponse(responseCode = "200", description = "Cập nhật điểm thành công")
    public ApiResponse<ScoreResponse> updateScore(@PathParam("id") Long id, @Valid ScoreRequest request) {
        return ApiResponse.success("Cập nhật điểm số thành công", scoreService.updateScore(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Xóa điểm số (Chỉ ADMIN)")
    @APIResponse(responseCode = "200", description = "Xóa điểm thành công")
    public ApiResponse<Void> deleteScore(@PathParam("id") Long id) {
        scoreService.deleteScore(id);
        return ApiResponse.success("Xóa điểm thành công", null);
    }
}