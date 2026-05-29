package com.studentmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ScoreSummaryResponse {
    public Long id;
    public Long studentId;
    public String studentName;
    public Long subjectId;
    public String subjectName;
    public BigDecimal averageScore;
    public LocalDateTime createdAt;
}
