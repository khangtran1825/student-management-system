// File: src/main/java/com/studentmanagement/service/ScoreService.java
package com.studentmanagement.service;

import com.studentmanagement.dto.request.ScoreRequest;
import com.studentmanagement.dto.response.ScoreResponse;
import com.studentmanagement.entity.Score;
import com.studentmanagement.entity.Student;
import com.studentmanagement.entity.Subject;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.ScoreRepository;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.repository.SubjectRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ScoreService {

    @Inject
    ScoreRepository scoreRepository;

    @Inject
    StudentRepository studentRepository;

    @Inject
    SubjectRepository subjectRepository;

    public List<ScoreResponse> getScoresByStudentId(Long studentId) {
        if (studentRepository.findByIdOptional(studentId).isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + studentId);
        }
        return scoreRepository.list("student.id", studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScoreResponse createScore(ScoreRequest request) {
        Student student = studentRepository.findByIdOptional(request.studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + request.studentId));

        Subject subject = subjectRepository.findByIdOptional(request.subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học với ID: " + request.subjectId));

        long count = scoreRepository.count("student.id = :studentId and subject.id = :subjectId",
                Parameters.with("studentId", request.studentId).and("subjectId", request.subjectId));
        if (count > 0) {
            throw new BusinessException("Sinh viên đã có đầu điểm cho môn học này.");
        }

        Score score = new Score();
        score.student = student;
        score.subject = subject;
        score.midtermScore = request.midtermScore;
        score.finalScore = request.finalScore;
        score.calculateAverage();

        scoreRepository.persist(score);
        return mapToResponse(score);
    }

    @Transactional
    public ScoreResponse updateScore(Long id, ScoreRequest request) {
        Score score = scoreRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy điểm số với ID: " + id));

        Student student = studentRepository.findByIdOptional(request.studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + request.studentId));

        Subject subject = subjectRepository.findByIdOptional(request.subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học với ID: " + request.subjectId));

        if (!score.student.id.equals(request.studentId) || !score.subject.id.equals(request.subjectId)) {
            long count = scoreRepository.count("student.id = :studentId and subject.id = :subjectId and id != :id",
                    Parameters.with("studentId", request.studentId).and("subjectId", request.subjectId).and("id", id));
            if (count > 0) {
                throw new BusinessException("Cặp sinh viên và môn học này đã tồn tại ở bản ghi khác.");
            }
        }

        score.student = student;
        score.subject = subject;
        score.midtermScore = request.midtermScore;
        score.finalScore = request.finalScore;
        score.calculateAverage();

        return mapToResponse(score);
    }

    @Transactional
    public void deleteScore(Long id) {
        Score score = scoreRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy điểm số với ID: " + id));
        scoreRepository.delete(score);
    }

    private ScoreResponse mapToResponse(Score score) {
        ScoreResponse response = new ScoreResponse();
        response.id = score.id;
        response.studentId = score.student.id;
        response.studentCode = score.student.studentCode;
        response.studentName = score.student.fullName;
        response.subjectId = score.subject.id;
        response.subjectCode = score.subject.subjectCode;
        response.subjectName = score.subject.subjectName;
        response.midtermScore = score.midtermScore;
        response.finalScore = score.finalScore;

        if (score.averageScore == null) {
            score.calculateAverage();
        }
        response.averageScore = score.averageScore;
        response.grade = calculateGrade(score.averageScore);
        response.createdAt = score.createdAt;
        response.updatedAt = score.updatedAt;
        return response;
    }

    private String calculateGrade(BigDecimal averageScore) {
        if (averageScore == null) return "F";
        double score = averageScore.doubleValue();
        if (score >= 8.5) return "A";
        if (score >= 7.0) return "B";
        if (score >= 5.5) return "C";
        if (score >= 4.0) return "D";
        return "F";
    }
}