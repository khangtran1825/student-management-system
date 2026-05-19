package com.studentmanagement.resource;

import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.dto.response.DashboardSummaryResponse;
import com.studentmanagement.service.DashboardService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Dashboard", description = "Thống kê tổng quan hệ thống")
public class DashboardResource {

    @Inject
    DashboardService dashboardService;

    @GET
    @Path("/summary")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Operation(summary = "Lấy thống kê tổng quan", description = "Dùng cho màn hình dashboard")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        return ApiResponse.success(dashboardService.getSummary());
    }
}