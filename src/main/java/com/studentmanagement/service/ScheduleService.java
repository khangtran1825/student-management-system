package com.studentmanagement.service;

import com.studentmanagement.dto.request.ScheduleRequest;
import com.studentmanagement.dto.response.ScheduleResponse;
import com.studentmanagement.entity.ClassEntity;
import com.studentmanagement.entity.Schedule;
import com.studentmanagement.entity.User;
import com.studentmanagement.entity.Subject;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.ClassRepository;
import com.studentmanagement.repository.ScheduleRepository;
import com.studentmanagement.repository.SubjectRepository;
import com.studentmanagement.entity.Teacher;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ScheduleService {

    @Inject
    ScheduleRepository repository;
    @Inject
    ClassRepository classRepository;
    @Inject
    SubjectRepository subjectRepository;

    public List<ScheduleResponse> getAll() {
        return repository.listAll().stream()
                .map(ScheduleResponse::new)
                .toList();
    }

        public List<ScheduleResponse> getByTeacherId(Long teacherId) {
                return repository.find("teacher.id = ?1", Sort.by("dayOfWeek").and("startTime"), teacherId).list()
                .stream()
                .map(ScheduleResponse::new)
                .toList();
    }

        public List<ClassEntity> getTeacherClasses(Long teacherId) {
                return repository.find("teacher.id = ?1", teacherId).list().stream()
                                .map(schedule -> schedule.classEntity)
                                .filter(java.util.Objects::nonNull)
                                .distinct()
                                .toList();
    }

        public List<ScheduleResponse> getByClassId(Long classId) {
                return repository.list("classEntity.id", classId).stream()
                                .map(ScheduleResponse::new)
                                .toList();
        }

    public ScheduleResponse getById(Long id) {
        Schedule entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        return new ScheduleResponse(entity);
    }

    @Transactional
    public ScheduleResponse create(ScheduleRequest request) {
        ClassEntity ce = classRepository.findByIdOptional(request.classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        Subject sub = subjectRepository.findByIdOptional(request.subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Teacher teacher = Teacher.findByIdOptional(request.teacherId)
                .map(t -> (Teacher) t)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Schedule entity = new Schedule();
        entity.classEntity = ce;
        entity.subject = sub;
        entity.teacher = teacher;
        entity.teacherName = teacher.fullName;
        entity.room = request.room;
        entity.dayOfWeek = request.dayOfWeek;
        entity.startTime = request.startTime;
        entity.endTime = request.endTime;
        repository.persist(entity);
        return new ScheduleResponse(entity);
    }

    @Transactional
    public ScheduleResponse update(Long id, ScheduleRequest request) {
        Schedule entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        ClassEntity ce = classRepository.findByIdOptional(request.classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        Subject sub = subjectRepository.findByIdOptional(request.subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Teacher teacher = Teacher.findByIdOptional(request.teacherId)
                .map(t -> (Teacher) t)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        entity.classEntity = ce;
        entity.subject = sub;
        entity.teacher = teacher;
        entity.teacherName = teacher.fullName;
        entity.room = request.room;
        entity.dayOfWeek = request.dayOfWeek;
        entity.startTime = request.startTime;
        entity.endTime = request.endTime;
        return new ScheduleResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Schedule entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        repository.delete(entity);
    }
}
