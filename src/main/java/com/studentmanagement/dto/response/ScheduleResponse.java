package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Schedule;
import java.time.LocalTime;

public class ScheduleResponse {
    public Long id;
    public ClassResponse classEntity;
    public SubjectResponse subject;
    public String teacherName;
    public String room;
    public String dayOfWeek;
    public LocalTime startTime;
    public LocalTime endTime;

    public ScheduleResponse(Schedule entity) {
        this.id = entity.id;
        if (entity.classEntity != null) {
            this.classEntity = new ClassResponse(entity.classEntity);
        }
        if (entity.subject != null) {
            this.subject = new SubjectResponse(entity.subject);
        }
        this.teacherName = entity.teacherName;
        this.room = entity.room;
        this.dayOfWeek = entity.dayOfWeek != null ? entity.dayOfWeek.name() : null;
        this.startTime = entity.startTime;
        this.endTime = entity.endTime;
    }
}
