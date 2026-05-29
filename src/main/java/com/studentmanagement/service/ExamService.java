package com.studentmanagement.service;

import com.studentmanagement.dto.request.ExamRequest;
import com.studentmanagement.dto.response.ExamResponse;
import com.studentmanagement.entity.Schedule;
import com.studentmanagement.entity.Exam;
import com.studentmanagement.entity.Semester;
import com.studentmanagement.entity.Subject;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.ExamRepository;
import com.studentmanagement.repository.ScheduleRepository;
import com.studentmanagement.repository.SemesterRepository;
import com.studentmanagement.repository.SubjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ExamService {

    @Inject
    ExamRepository repository;
    @Inject
    SubjectRepository subjectRepository;
    @Inject
    SemesterRepository semesterRepository;
        @Inject
        ScheduleRepository scheduleRepository;

    public List<ExamResponse> getAll() {
        return repository.listAll().stream()
                .map(ExamResponse::new)
                .toList();
    }

        public List<ExamResponse> getByTeacherId(Long teacherId) {
                List<Schedule> schedules = scheduleRepository.find("teacher.id = ?1", teacherId).list();
        Set<Long> subjectIds = schedules.stream()
                .map(schedule -> schedule.subject)
                .filter(java.util.Objects::nonNull)
                .map(subject -> subject.id)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return repository.listAll().stream()
                .filter(exam -> exam.subject != null && subjectIds.contains(exam.subject.id))
                .map(exam -> {
                    List<String> classNames = schedules.stream()
                            .filter(schedule -> schedule.subject != null && schedule.subject.id.equals(exam.subject.id))
                            .map(schedule -> schedule.classEntity != null ? schedule.classEntity.className : null)
                            .filter(java.util.Objects::nonNull)
                            .distinct()
                            .toList();
                    return new ExamResponse(exam, classNames);
                })
                .sorted(java.util.Comparator.comparing(e -> e.examDate))
                .toList();
    }

    public ExamResponse getById(Long id) {
        Exam entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        return new ExamResponse(entity);
    }

    @Transactional
    public ExamResponse create(ExamRequest request) {
        Subject sub = subjectRepository.findByIdOptional(request.subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Semester sem = semesterRepository.findByIdOptional(request.semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        Exam entity = new Exam();
        entity.subject = sub;
        entity.semester = sem;
        entity.examDate = request.examDate;
        entity.room = request.room;
        repository.persist(entity);
        return new ExamResponse(entity);
    }

    @Transactional
    public ExamResponse update(Long id, ExamRequest request) {
        Exam entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        Subject sub = subjectRepository.findByIdOptional(request.subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Semester sem = semesterRepository.findByIdOptional(request.semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        entity.subject = sub;
        entity.semester = sem;
        entity.examDate = request.examDate;
        entity.room = request.room;
        return new ExamResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Exam entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        repository.delete(entity);
    }
}
