package com.studentmanagement.dto.response;

import java.time.LocalDateTime;

public class DashboardSummaryResponse {
    public long totalStudents;
    public long totalClasses;
    public long totalSubjects;
    public long totalScores;
    public long totalUsers;
    public long activeUsers;
    public long totalTeachers;
    public long totalAdmins;
    public LocalDateTime generatedAt;
}