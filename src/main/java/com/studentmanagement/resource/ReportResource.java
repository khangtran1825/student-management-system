package com.studentmanagement.resource;

import com.studentmanagement.service.ReportService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/reports")
@Tag(name = "Reports", description = "Xuất báo cáo PDF/Excel")
public class ReportResource {

    @Inject
    ReportService reportService;

    @GET
    @Path("/student/{studentId}/transcript.pdf")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Produces("application/pdf")
    @Operation(summary = "Xuất bảng điểm sinh viên (PDF)")
    @APIResponse(responseCode = "200", description = "File PDF")
    public Response getStudentTranscriptPdf(@PathParam("studentId") Long studentId) {
        byte[] pdfData = reportService.generateStudentTranscriptPdf(studentId);
        
        return Response.ok(pdfData)
                .header("Content-Disposition", "attachment; filename=\"transcript-" + studentId + ".pdf\"")
                .header("Content-Type", "application/pdf")
                .build();
    }

    @GET
    @Path("/student/{studentId}/transcript.xlsx")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Operation(summary = "Xuất bảng điểm sinh viên (Excel)")
    @APIResponse(responseCode = "200", description = "File Excel")
    public Response getStudentTranscriptExcel(@PathParam("studentId") Long studentId) {
        byte[] excelData = reportService.generateStudentTranscriptExcel(studentId);
        
        return Response.ok(excelData)
                .header("Content-Disposition", "attachment; filename=\"transcript-" + studentId + ".xlsx\"")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build();
    }

    @GET
    @Path("/class/{classId}/grades.xlsx")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Operation(summary = "Xuất báo cáo điểm lớp học (Excel)")
    @APIResponse(responseCode = "200", description = "File Excel")
    public Response getClassGradesExcel(@PathParam("classId") Long classId) {
        byte[] excelData = reportService.generateClassGradesExcel(classId);
        
        return Response.ok(excelData)
                .header("Content-Disposition", "attachment; filename=\"class-grades-" + classId + ".xlsx\"")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build();
    }

    @GET
    @Path("/attendance/{classId}.pdf")
    @RolesAllowed({"ADMIN", "TEACHER"})
    @Produces("application/pdf")
    @Operation(summary = "Xuất báo cáo điểm danh lớp (PDF)")
    @APIResponse(responseCode = "200", description = "File PDF")
    public Response getAttendanceReportPdf(@PathParam("classId") Long classId,
                                          @QueryParam("startDate") String startDate,
                                          @QueryParam("endDate") String endDate) {
        byte[] pdfData = reportService.generateAttendanceReportPdf(classId, startDate, endDate);
        
        return Response.ok(pdfData)
                .header("Content-Disposition", "attachment; filename=\"attendance-report-" + classId + ".pdf\"")
                .header("Content-Type", "application/pdf")
                .build();
    }
}