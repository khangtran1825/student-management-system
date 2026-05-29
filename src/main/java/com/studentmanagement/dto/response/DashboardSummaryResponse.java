package com.studentmanagement.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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

    // New detailed parts
    public List<StudentSummaryResponse> recentStudents;
    public List<ScoreSummaryResponse> recentScores;
    public List<ExamSummaryResponse> upcomingExams;

    // Attendance summary for today
    public long attendanceTodayPresent;
    public long attendanceTodayAbsent;
    public long attendanceTodayExcused;
}