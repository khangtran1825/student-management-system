package com.studentmanagement.dto.response;

import java.time.LocalDateTime;

public class ExamSummaryResponse {
    public Long id;
    public Long subjectId;
    public String subjectName;
    public LocalDateTime examDate;
    public String room;
}
