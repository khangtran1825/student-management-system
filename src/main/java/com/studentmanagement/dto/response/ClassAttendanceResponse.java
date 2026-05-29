package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Student;
import com.studentmanagement.entity.Attendance;

public class ClassAttendanceResponse {
    public Long studentId;
    public String studentCode;
    public String fullName;
    public Long attendanceId;
    public String status;
    public String note;

    public ClassAttendanceResponse(Student student, Attendance attendance) {
        this.studentId = student.id;
        this.studentCode = student.studentCode;
        this.fullName = student.fullName;
        
        if (attendance != null) {
            this.attendanceId = attendance.id;
            this.status = attendance.status != null ? attendance.status.name() : null;
            this.note = attendance.note;
        } else {
            this.attendanceId = null;
            this.status = null;
            this.note = null;
        }
    }
}
