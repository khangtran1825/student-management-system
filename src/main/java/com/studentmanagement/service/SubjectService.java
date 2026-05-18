package com.studentmanagement.service;

import com.studentmanagement.dto.request.SubjectRequest;
import com.studentmanagement.dto.response.PageResponse;
import com.studentmanagement.dto.response.SubjectResponse;
import com.studentmanagement.entity.Subject;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.SubjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SubjectService {

    @Inject
    SubjectRepository subjectRepository;

    public List<SubjectResponse> getAll() {
        return subjectRepository.listAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<SubjectResponse> search(String keyword, int page, int size) {
        String kw = keyword == null ? "" : keyword;
        List<SubjectResponse> content = subjectRepository.findByKeyword(kw, page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        long total = subjectRepository.countByKeyword(kw);
        return new PageResponse<>(content, page, size, total);
    }

    public SubjectResponse getById(Long id) {
        Subject entity = subjectRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học với ID: " + id));
        return mapToResponse(entity);
    }

    @Transactional
    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsBySubjectCode(request.subjectCode)) {
            throw new BusinessException("Mã môn học đã tồn tại: " + request.subjectCode);
        }

        Subject entity = new Subject();
        entity.subjectCode = request.subjectCode;
        entity.subjectName = request.subjectName;
        entity.credits = request.credits;

        subjectRepository.persist(entity);
        return mapToResponse(entity);
    }

    @Transactional
    public SubjectResponse update(Long id, SubjectRequest request) {
        Subject entity = subjectRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học với ID: " + id));

        if (subjectRepository.existsBySubjectCodeAndIdNot(request.subjectCode, id)) {
            throw new BusinessException("Mã môn học đã tồn tại: " + request.subjectCode);
        }

        entity.subjectCode = request.subjectCode;
        entity.subjectName = request.subjectName;
        entity.credits = request.credits;

        return mapToResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Subject entity = subjectRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học với ID: " + id));

        long scoreCount = subjectRepository.getEntityManager()
                .createQuery("SELECT COUNT(sc) FROM Score sc WHERE sc.subject.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();

        if (scoreCount > 0) {
            throw new BusinessException("Không thể xóa môn học đang có " + scoreCount + " điểm số liên quan.");
        }

        subjectRepository.delete(entity);
    }

    private SubjectResponse mapToResponse(Subject entity) {
        SubjectResponse response = new SubjectResponse();
        response.id = entity.id;
        response.subjectCode = entity.subjectCode;
        response.subjectName = entity.subjectName;
        response.credits = entity.credits;
        response.createdAt = entity.createdAt;
        response.updatedAt = entity.updatedAt;
        return response;
    }
}