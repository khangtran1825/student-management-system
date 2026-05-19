package com.studentmanagement.service;

import com.studentmanagement.dto.request.AcademicYearRequest;
import com.studentmanagement.dto.response.AcademicYearResponse;
import com.studentmanagement.entity.AcademicYear;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.AcademicYearRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AcademicYearService {

    @Inject
    AcademicYearRepository repository;

    public List<AcademicYearResponse> getAll() {
        return repository.findAll().stream()
                .map(AcademicYearResponse::new)
                .collect(Collectors.toList());
    }

    public AcademicYearResponse getById(Long id) {
        AcademicYear entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        return new AcademicYearResponse(entity);
    }

    @Transactional
    public AcademicYearResponse create(AcademicYearRequest request) {
        if (repository.count("name", request.name) > 0) {
            throw new BusinessException("Name exists: " + request.name);
        }
        AcademicYear entity = new AcademicYear();
        entity.name = request.name;
        entity.startDate = request.startDate;
        entity.endDate = request.endDate;
        repository.persist(entity);
        return new AcademicYearResponse(entity);
    }

    @Transactional
    public AcademicYearResponse update(Long id, AcademicYearRequest request) {
        AcademicYear entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        if (!entity.name.equals(request.name) && repository.count("name", request.name) > 0) {
            throw new BusinessException("Name exists: " + request.name);
        }
        entity.name = request.name;
        entity.startDate = request.startDate;
        entity.endDate = request.endDate;
        return new AcademicYearResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        AcademicYear entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        repository.delete(entity);
    }
}
