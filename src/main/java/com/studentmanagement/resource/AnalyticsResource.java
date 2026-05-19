package com.studentmanagement.resource;

import com.studentmanagement.dto.response.ApiResponse;
import com.studentmanagement.service.AnalyticsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Map;

@Path("/api/analytics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Analytics & Dashboard", description = "Thống kê dữ liệu hệ thống")
public class AnalyticsResource {

    @Inject
    AnalyticsService analyticsService;

    @GET
    @Path("/grade-distribution")
    @Operation(summary = "Thống kê phân bổ điểm số (A, B, C, D, F)")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<Map<String, Long>> getGradeDistribution() {
        return ApiResponse.success("Thành công", analyticsService.getGradeDistribution());
    }

    @GET
    @Path("/pass-rate")
    @Operation(summary = "Thống kê tỷ lệ đỗ/trượt")
    @APIResponse(responseCode = "200", description = "Thành công")
    public ApiResponse<Map<String, Object>> getPassRate() {
        return ApiResponse.success("Thành công", analyticsService.getPassRate());
    }
}
