package com.studentmanagement.service;

import com.studentmanagement.dto.response.DashboardSummaryResponse;
import com.studentmanagement.dto.response.StudentSummaryResponse;
import com.studentmanagement.dto.response.ScoreSummaryResponse;
import com.studentmanagement.dto.response.ExamSummaryResponse;
import com.studentmanagement.entity.Attendance;
import com.studentmanagement.entity.User;
import io.quarkus.panache.common.Page;
import jakarta.inject.Named;
import com.studentmanagement.repository.ClassRepository;
import com.studentmanagement.repository.ScoreRepository;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.repository.SubjectRepository;
import com.studentmanagement.repository.UserRepository;
import com.studentmanagement.repository.ExamRepository;
import com.studentmanagement.repository.AttendanceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@ApplicationScoped
public class DashboardService {

    @Inject
    StudentRepository studentRepository;

    @Inject
    ClassRepository classRepository;

    @Inject
    SubjectRepository subjectRepository;

    @Inject
    ScoreRepository scoreRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    AttendanceRepository attendanceRepository;

    @Inject
    UserRepository userRepository;

    public DashboardSummaryResponse getSummary() {
        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.totalStudents = studentRepository.count();
        response.totalClasses = classRepository.count();
        response.totalSubjects = subjectRepository.count();
        response.totalScores = scoreRepository.count();
        response.totalUsers = userRepository.count();
        response.activeUsers = userRepository.count("active", true);
        response.totalTeachers = userRepository.count("role in (?1, ?2)", "TEACHER", "ROLE_TEACHER");
        response.totalAdmins = userRepository.count("role in (?1, ?2)", "ADMIN", "ROLE_ADMIN");
        response.generatedAt = LocalDateTime.now();

        // recent students (latest 5)
        var recentStudents = studentRepository.find("ORDER BY createdAt desc").page(Page.of(0,5)).list();
        response.recentStudents = recentStudents.stream().map(s -> {
            StudentSummaryResponse ss = new StudentSummaryResponse();
            ss.id = s.id;
            ss.studentCode = s.studentCode;
            ss.fullName = s.fullName;
            ss.className = s.classEntity != null ? s.classEntity.className : null;
            ss.createdAt = s.createdAt;
            return ss;
        }).toList();

        // recent scores (latest 5)
        var recentScores = scoreRepository.findAll().page(Page.of(0,5)).list();
        response.recentScores = recentScores.stream().map(sc -> {
            ScoreSummaryResponse rs = new ScoreSummaryResponse();
            rs.id = sc.id;
            rs.studentId = sc.student != null ? sc.student.id : null;
            rs.studentName = sc.student != null ? sc.student.fullName : null;
            rs.subjectId = sc.subject != null ? sc.subject.id : null;
            rs.subjectName = sc.subject != null ? sc.subject.subjectName : null;
            rs.averageScore = sc.averageScore;
            rs.createdAt = sc.createdAt;
            return rs;
        }).toList();

        // upcoming exams (next 5)
        var now = LocalDateTime.now();
        var upcomingExams = examRepository.find("examDate >= ?1 ORDER BY examDate asc", now).page(Page.of(0,5)).list();
        response.upcomingExams = upcomingExams.stream().map(ex -> {
            ExamSummaryResponse er = new ExamSummaryResponse();
            er.id = ex.id;
            er.subjectId = ex.subject != null ? ex.subject.id : null;
            er.subjectName = ex.subject != null ? ex.subject.subjectName : null;
            er.examDate = ex.examDate;
            er.room = ex.room;
            return er;
        }).toList();

        // attendance today breakdown
        var today = java.time.LocalDate.now();
        response.attendanceTodayPresent = attendanceRepository.count("date = ?1 and status = ?2", today, Attendance.AttendanceStatus.PRESENT);
        response.attendanceTodayAbsent = attendanceRepository.count("date = ?1 and status = ?2", today, Attendance.AttendanceStatus.ABSENT);
        response.attendanceTodayExcused = attendanceRepository.count("date = ?1 and status = ?2", today, Attendance.AttendanceStatus.EXCUSED);
        return response;
    }
}