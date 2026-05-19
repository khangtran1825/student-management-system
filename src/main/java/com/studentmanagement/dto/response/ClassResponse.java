package com.studentmanagement.dto.response;

import com.studentmanagement.entity.ClassEntity;

import java.time.LocalDateTime;

public class ClassResponse {
    public Long id;
    public String classCode;
    public String className;
    public String major;
    public Integer studentCount;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public ClassResponse() {
    }

    public ClassResponse(ClassEntity entity) {
        this.id = entity.id;
        this.classCode = entity.classCode;
        this.className = entity.className;
        this.major = entity.major;
        this.studentCount = entity.students != null ? entity.students.size() : null;
        this.createdAt = entity.createdAt;
        this.updatedAt = entity.updatedAt;
    }
}
