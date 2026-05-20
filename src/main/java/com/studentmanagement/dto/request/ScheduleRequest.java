package com.studentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import com.studentmanagement.entity.Schedule.DayOfWeek;

public class ScheduleRequest {
    @NotNull
    public Long classId;
    @NotNull
    public Long subjectId;
    @NotBlank
    public String teacherName;
    @NotBlank
    public String room;
    @NotNull
    public DayOfWeek dayOfWeek;
    @NotNull
    public LocalTime startTime;
    @NotNull
    public LocalTime endTime;
}
