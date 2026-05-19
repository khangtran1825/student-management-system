package com.studentmanagement.service;

import com.studentmanagement.dto.request.AttendanceRequest;
import com.studentmanagement.dto.response.AttendanceResponse;
import com.studentmanagement.entity.Attendance;
import com.studentmanagement.entity.Schedule;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.AttendanceRepository;
import com.studentmanagement.repository.ScheduleRepository;
import com.studentmanagement.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AttendanceService {

    @Inject
    AttendanceRepository repository;
    @Inject
    StudentRepository studentRepository;
    @Inject
    ScheduleRepository scheduleRepository;

    public List<AttendanceResponse> getAll() {
        return repository.findAll().stream()
                .map(AttendanceResponse::new)
                .collect(Collectors.toList());
    }

    public AttendanceResponse getById(Long id) {
        Attendance entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        return new AttendanceResponse(entity);
    }

    @Transactional
    public AttendanceResponse create(AttendanceRequest request) {
        Student st = studentRepository.findByIdOptional(request.studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Schedule sch = scheduleRepository.findByIdOptional(request.scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        if (repository.count("student.id = ?1 and schedule.id = ?2 and date = ?3", 
                request.studentId, request.scheduleId, request.date) > 0) {
            throw new BusinessException("Attendance already marked for this date");
        }

        Attendance entity = new Attendance();
        entity.student = st;
        entity.schedule = sch;
        entity.date = request.date;
        entity.status = request.status;
        entity.note = request.note;
        repository.persist(entity);
        return new AttendanceResponse(entity);
    }

    @Transactional
    public AttendanceResponse update(Long id, AttendanceRequest request) {
        Attendance entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        
        entity.status = request.status;
        entity.note = request.note;
        return new AttendanceResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Attendance entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        repository.delete(entity);
    }
}
