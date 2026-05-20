package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Attendance;
import java.time.LocalDate;

public class AttendanceResponse {
    public Long id;
    public StudentResponse student;
    public ScheduleResponse schedule;
    public LocalDate date;
    public String status;
    public String note;

    public AttendanceResponse(Attendance entity) {
        this.id = entity.id;
        if (entity.student != null) {
            this.student = new StudentResponse(entity.student);
        }
        if (entity.schedule != null) {
            this.schedule = new ScheduleResponse(entity.schedule);
        }
        this.date = entity.date;
        this.status = entity.status != null ? entity.status.name() : null;
        this.note = entity.note;
    }
}
