package com.studentmanagement.service;

import com.studentmanagement.dto.response.DashboardSummaryResponse;
import com.studentmanagement.entity.User;
import com.studentmanagement.repository.ClassRepository;
import com.studentmanagement.repository.ScoreRepository;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.repository.SubjectRepository;
import com.studentmanagement.repository.UserRepository;
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
        return response;
    }
}