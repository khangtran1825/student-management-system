// File: src/main/java/com/studentmanagement/service/ClassService.java
package com.studentmanagement.service;

import com.studentmanagement.dto.request.ClassRequest;
import com.studentmanagement.dto.response.ClassResponse;
import com.studentmanagement.entity.ClassEntity;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.ClassRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClassService {

    @Inject
    ClassRepository classRepository;

    public List<ClassResponse> getAll() {
        return classRepository.listAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ClassResponse getById(Long id) {
        ClassEntity entity = classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + id));
        return mapToResponse(entity);
    }

    @Transactional
    public ClassResponse create(ClassRequest request) {
        ClassEntity entity = new ClassEntity();
        entity.classCode = request.classCode;
        entity.className = request.className;
        entity.major = request.major;

        classRepository.persist(entity);
        return mapToResponse(entity);
    }

    @Transactional
    public ClassResponse update(Long id, ClassRequest request) {
        ClassEntity entity = classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + id));

        entity.classCode = request.classCode;
        entity.className = request.className;
        entity.major = request.major;

        return mapToResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        ClassEntity entity = classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + id));
        classRepository.delete(entity);
    }

    private ClassResponse mapToResponse(ClassEntity entity) {
        ClassResponse response = new ClassResponse();
        response.id = entity.id;
        response.classCode = entity.classCode;
        response.className = entity.className;
        response.major = entity.major;
        response.studentCount = entity.students != null ? entity.students.size() : 0;
        response.createdAt = entity.createdAt;
        response.updatedAt = entity.updatedAt;
        return response;
    }
}