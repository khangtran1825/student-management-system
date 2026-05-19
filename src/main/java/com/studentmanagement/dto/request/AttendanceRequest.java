package com.studentmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import com.studentmanagement.entity.Attendance.AttendanceStatus;

public class AttendanceRequest {
    @NotNull
    public Long studentId;
    @NotNull
    public Long scheduleId;
    @NotNull
    public LocalDate date;
    @NotNull
    public AttendanceStatus status;
    public String note;
}
