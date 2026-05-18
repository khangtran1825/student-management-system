// File: src/main/java/com/studentmanagement/service/StudentService.java
package com.studentmanagement.service;

import com.studentmanagement.dto.request.StudentRequest;
import com.studentmanagement.dto.response.PageResponse;
import com.studentmanagement.dto.response.StudentResponse;
import com.studentmanagement.entity.ClassEntity;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.ClassRepository;
import com.studentmanagement.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StudentService {

    @Inject
    StudentRepository studentRepository;

    @Inject
    ClassRepository classRepository;

    public StudentResponse getById(Long id) {
        Student entity = studentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id));
        return mapToResponse(entity);
    }

    public PageResponse<StudentResponse> searchStudents(String keyword, int page, int size) {
        String searchKeyword = (keyword == null) ? "" : keyword;
        List<Student> students = studentRepository.findByNameOrCode(searchKeyword, page, size);
        long totalElements = studentRepository.countByNameOrCode(searchKeyword);

        List<StudentResponse> content = students.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(content, page, size, totalElements);
    }

    @Transactional
    public StudentResponse create(StudentRequest request) {
        validateDuplicate(request.studentCode, request.email, null);

        ClassEntity classEntity = classRepository.findByIdOptional(request.classId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + request.classId));

        Student entity = new Student();
        updateEntityFromRequest(entity, request, classEntity);

        studentRepository.persist(entity);
        return mapToResponse(entity);
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student entity = studentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id));

        validateDuplicate(request.studentCode, request.email, id);

        ClassEntity classEntity = classRepository.findByIdOptional(request.classId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + request.classId));

        updateEntityFromRequest(entity, request, classEntity);

        return mapToResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Student entity = studentRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id));
        studentRepository.delete(entity);
    }

    private void validateDuplicate(String studentCode, String email, Long currentId) {
        studentRepository.find("studentCode", studentCode).firstResultOptional().ifPresent(student -> {
            if (currentId == null || !student.id.equals(currentId)) {
                throw new BusinessException("Mã sinh viên đã tồn tại: " + studentCode);
            }
        });

        studentRepository.find("email", email).firstResultOptional().ifPresent(student -> {
            if (currentId == null || !student.id.equals(currentId)) {
                throw new BusinessException("Email đã tồn tại: " + email);
            }
        });
    }

    private void updateEntityFromRequest(Student entity, StudentRequest request, ClassEntity classEntity) {
        entity.studentCode = request.studentCode;
        entity.fullName = request.fullName;
        entity.gender = request.gender;
        entity.dateOfBirth = request.dateOfBirth;
        entity.email = request.email;
        entity.phone = request.phone;
        entity.address = request.address;
        entity.classEntity = classEntity;
    }

    private StudentResponse mapToResponse(Student entity) {
        StudentResponse response = new StudentResponse();
        response.id = entity.id;
        response.studentCode = entity.studentCode;
        response.fullName = entity.fullName;
        response.gender = entity.gender;
        response.dateOfBirth = entity.dateOfBirth;
        response.email = entity.email;
        response.phone = entity.phone;
        response.address = entity.address;

        if (entity.classEntity != null) {
            response.classId = entity.classEntity.id;
            response.classCode = entity.classEntity.classCode;
            response.className = entity.classEntity.className;
        }

        response.createdAt = entity.createdAt;
        response.updatedAt = entity.updatedAt;
        return response;
    }
}