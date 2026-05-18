package com.studentmanagement.dto.response;

import java.time.LocalDateTime;

public class ClassResponse {
    public Long id;
    public String classCode;
    public String className;
    public String major;
    public Integer studentCount;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
