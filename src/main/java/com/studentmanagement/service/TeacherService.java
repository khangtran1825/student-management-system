package com.studentmanagement.service;

import com.studentmanagement.dto.request.TeacherRequest;
import com.studentmanagement.dto.response.PagedResponse;
import com.studentmanagement.dto.response.TeacherResponse;
import com.studentmanagement.entity.Teacher;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.TeacherRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TeacherService {

    @Inject
    TeacherRepository repository;

    public PagedResponse<TeacherResponse> getAll(int page, int size, String search) {
        List<Teacher> teachers;
        long totalElements;

        if (search != null && !search.trim().isEmpty()) {
            teachers = repository.findByNameOrCode(search.trim(), page, size);
            totalElements = repository.countByNameOrCode(search.trim());
        } else {
            teachers = repository.findAll().page(page, size).list();
            totalElements = repository.count();
        }

        List<TeacherResponse> content = teachers.stream()
                .map(TeacherResponse::new)
                .toList();

        return new PagedResponse<>(content, totalElements, page, size);
    }

    public TeacherResponse getById(Long id) {
        Teacher entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found teacher with id: " + id));
        return new TeacherResponse(entity);
    }

    @Transactional
    public TeacherResponse create(TeacherRequest request) {
        if (repository.findByTeacherCode(request.teacherCode) != null) {
            throw new BusinessException("Mã giảng viên đã tồn tại");
        }
        if (repository.find("email", request.email).firstResult() != null) {
            throw new BusinessException("Email đã được sử dụng");
        }

        Teacher entity = new Teacher();
        entity.teacherCode = request.teacherCode;
        entity.fullName = request.fullName;
        entity.gender = Teacher.Gender.valueOf(request.gender);
        entity.dateOfBirth = request.dateOfBirth;
        entity.email = request.email;
        entity.phone = request.phone;
        entity.department = request.department;

        repository.persist(entity);
        return new TeacherResponse(entity);
    }

    @Transactional
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found teacher with id: " + id));

        Teacher existingByCode = repository.findByTeacherCode(request.teacherCode);
        if (existingByCode != null && !existingByCode.id.equals(id)) {
            throw new BusinessException("Mã giảng viên đã tồn tại");
        }
        Teacher existingByEmail = repository.find("email", request.email).firstResult();
        if (existingByEmail != null && !existingByEmail.id.equals(id)) {
            throw new BusinessException("Email đã được sử dụng");
        }

        entity.teacherCode = request.teacherCode;
        entity.fullName = request.fullName;
        entity.gender = Teacher.Gender.valueOf(request.gender);
        entity.dateOfBirth = request.dateOfBirth;
        entity.email = request.email;
        entity.phone = request.phone;
        entity.department = request.department;

        return new TeacherResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Teacher entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found teacher with id: " + id));
        repository.delete(entity);
    }
}
