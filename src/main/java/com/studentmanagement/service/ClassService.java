package com.studentmanagement.service;

import com.studentmanagement.dto.request.ClassRequest;
import com.studentmanagement.dto.response.ClassResponse;
import com.studentmanagement.dto.response.PageResponse;
import com.studentmanagement.entity.ClassEntity;
import com.studentmanagement.exception.BusinessException;
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

    /**
     * Lấy tất cả lớp học — dùng JOIN COUNT để tránh N+1 query.
     */
    public List<ClassResponse> getAll() {
        return classRepository.findAllWithStudentCount().stream()
                .map(row -> {
                    ClassEntity entity = (ClassEntity) row[0];
                    Long count = (Long) row[1];
                    return mapToResponse(entity, count);
                })
                .collect(Collectors.toList());
    }

    /**
     * Phân trang lớp học.
     */
    public PageResponse<ClassResponse> getPage(int page, int size) {
        long total = classRepository.count();
        List<ClassResponse> content = classRepository
                .findAll()
                .page(page, size)
                .list()
                .stream()
                .map(e -> mapToResponse(e, null))
                .collect(Collectors.toList());
        return new PageResponse<>(content, page, size, total);
    }

    public ClassResponse getById(Long id) {
        ClassEntity entity = classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + id));

        // Đếm riêng để tránh lazy load
        long studentCount = classRepository.getEntityManager()
                .createQuery("SELECT COUNT(s) FROM Student s WHERE s.classEntity.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return mapToResponse(entity, studentCount);
    }

    @Transactional
    public ClassResponse create(ClassRequest request) {
        if (classRepository.existsByClassCode(request.classCode)) {
            throw new BusinessException("Mã lớp học đã tồn tại: " + request.classCode);
        }

        ClassEntity entity = new ClassEntity();
        entity.classCode = request.classCode;
        entity.className = request.className;
        entity.major = request.major;

        classRepository.persist(entity);
        return mapToResponse(entity, 0L);
    }

    @Transactional
    public ClassResponse update(Long id, ClassRequest request) {
        ClassEntity entity = classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + id));

        if (classRepository.existsByClassCodeAndIdNot(request.classCode, id)) {
            throw new BusinessException("Mã lớp học đã tồn tại: " + request.classCode);
        }

        entity.classCode = request.classCode;
        entity.className = request.className;
        entity.major = request.major;

        long studentCount = classRepository.getEntityManager()
                .createQuery("SELECT COUNT(s) FROM Student s WHERE s.classEntity.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return mapToResponse(entity, studentCount);
    }

    @Transactional
    public void delete(Long id) {
        ClassEntity entity = classRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + id));

        long studentCount = classRepository.getEntityManager()
                .createQuery("SELECT COUNT(s) FROM Student s WHERE s.classEntity.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();

        if (studentCount > 0) {
            throw new BusinessException("Không thể xóa lớp học đang có " + studentCount + " sinh viên.");
        }

        classRepository.delete(entity);
    }

    private ClassResponse mapToResponse(ClassEntity entity, Long studentCount) {
        ClassResponse response = new ClassResponse();
        response.id = entity.id;
        response.classCode = entity.classCode;
        response.className = entity.className;
        response.major = entity.major;
        response.studentCount = studentCount != null ? studentCount.intValue() : 0;
        response.createdAt = entity.createdAt;
        response.updatedAt = entity.updatedAt;
        return response;
    }
}