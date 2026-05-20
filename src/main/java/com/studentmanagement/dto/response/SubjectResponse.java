package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Subject;

import java.time.LocalDateTime;

public class SubjectResponse {
    public Long id;
    public String subjectCode;
    public String subjectName;
    public Integer credits;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public SubjectResponse() {
    }

    public SubjectResponse(Subject entity) {
        this.id = entity.id;
        this.subjectCode = entity.subjectCode;
        this.subjectName = entity.subjectName;
        this.credits = entity.credits;
        this.createdAt = entity.createdAt;
        this.updatedAt = entity.updatedAt;
    }
}
