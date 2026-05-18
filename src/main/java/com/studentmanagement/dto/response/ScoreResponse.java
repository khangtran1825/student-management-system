package com.studentmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ScoreResponse {
    public Long id;
    public Long studentId;
    public String studentCode;
    public String studentName;
    public Long subjectId;
    public String subjectCode;
    public String subjectName;
    public BigDecimal midtermScore;
    public BigDecimal finalScore;
    public BigDecimal averageScore;
    public String grade;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
