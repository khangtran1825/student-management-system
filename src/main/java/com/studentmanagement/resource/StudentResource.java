// File: src/main/java/com/studentmanagement/resource/StudentResource.java
package com.studentmanagement.resource;

import com.studentmanagement.dto.request.StudentRequest;
import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.PageResponse;
import com.studentmanagement.dto.response.StudentResponse;
import com.studentmanagement.entity.User;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.repository.UserRepository;
import com.studentmanagement.service.StudentService;
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

@Path("/api/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Student Management", description = "Quản lý hồ sơ thông tin sinh viên")
public class StudentResource {

    @Inject
    StudentService studentService;

    @Inject
    UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Tìm kiếm và phân trang sinh viên", description = "Dành cho Admin và Teacher")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<PageResponse<StudentResponse>> search(
            @QueryParam("keyword") String keyword,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return ApiResponse.success(studentService.searchStudents(keyword, page, size));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Lấy chi tiết sinh viên", description = "Admin/Teacher xem bất kỳ, Student chỉ xem chính mình")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "400", description = "Lỗi phân quyền dữ liệu cá nhân")
    public ApiResponse<StudentResponse> getById(@PathParam("id") Long id, @Context SecurityContext sec) {
        if (sec.isUserInRole("STUDENT")) {
            String username = sec.getUserPrincipal().getName();
            User user = userRepository.find("username", username).firstResult();
            if (user == null || user.student == null || !user.student.id.equals(id)) {
                throw new BusinessException("Bạn không có quyền xem thông tin của sinh viên khác.");
            }
        }
        return ApiResponse.success(studentService.getById(id));
    }

    @POST
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Thêm mới sinh viên")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<StudentResponse> create(@Valid StudentRequest request) {
        return ApiResponse.success("Thêm mới sinh viên thành công", studentService.create(request));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Cập nhật hồ sơ sinh viên")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<StudentResponse> update(@PathParam("id") Long id, @Valid StudentRequest request) {
        return ApiResponse.success("Cập nhật sinh viên thành công", studentService.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Xóa sinh viên khỏi hệ thống (Chỉ ADMIN)")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        studentService.delete(id);
        return ApiResponse.success("Xóa sinh viên thành công", null);
    }
}