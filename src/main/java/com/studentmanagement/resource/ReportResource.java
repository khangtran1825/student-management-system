package com.studentmanagement.resource;

import com.studentmanagement.service.ReportService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/reports")
@Tag(name = "Report Management", description = "Quản lý xuất báo cáo học tập")
public class ReportResource {

    @Inject
    ReportService reportService;

    @GET
    @Path("/student/{id}/transcript/pdf")
    @Produces("application/pdf")
    @Operation(summary = "Tải bảng điểm sinh viên định dạng PDF")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy sinh viên")
    public Response downloadTranscriptPdf(@PathParam("id") Long id) {
        byte[] pdfBytes = reportService.generateStudentTranscriptPdf(id);
        return Response.ok(pdfBytes)
                .header("Content-Disposition", "attachment; filename=\"transcript_" + id + ".pdf\"")
                .build();
    }

    @GET
    @Path("/student/{id}/transcript/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Operation(summary = "Tải bảng điểm sinh viên định dạng Excel")
    @APIResponse(responseCode = "200", description = "Thành công")
    @APIResponse(responseCode = "404", description = "Không tìm thấy sinh viên")
    public Response downloadTranscriptExcel(@PathParam("id") Long id) {
        byte[] excelBytes = reportService.generateStudentTranscriptExcel(id);
        return Response.ok(excelBytes)
                .header("Content-Disposition", "attachment; filename=\"transcript_" + id + ".xlsx\"")
                .build();
    }
}
