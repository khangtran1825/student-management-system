package com.studentmanagement.dto.response;

import java.time.LocalDateTime;

public class SubjectResponse {
    public Long id;
    public String subjectCode;
    public String subjectName;
    public Integer credits;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
