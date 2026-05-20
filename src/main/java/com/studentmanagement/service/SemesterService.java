package com.studentmanagement.service;

import com.studentmanagement.dto.request.SemesterRequest;
import com.studentmanagement.dto.response.SemesterResponse;
import com.studentmanagement.entity.AcademicYear;
import com.studentmanagement.entity.Semester;
import com.studentmanagement.exception.BusinessException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.AcademicYearRepository;
import com.studentmanagement.repository.SemesterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SemesterService {

    @Inject
    SemesterRepository repository;
    
    @Inject
    AcademicYearRepository academicYearRepository;

    public List<SemesterResponse> getAll() {
    return repository.listAll().stream()
        .map(SemesterResponse::new)
        .toList();
    }

    public SemesterResponse getById(Long id) {
        Semester entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        return new SemesterResponse(entity);
    }

    @Transactional
    public SemesterResponse create(SemesterRequest request) {
        AcademicYear ay = academicYearRepository.findByIdOptional(request.academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYear not found"));
        if (repository.count("name = ?1 and academicYear.id = ?2", request.name, request.academicYearId) > 0) {
            throw new BusinessException("Semester exists in this academic year");
        }
        Semester entity = new Semester();
        entity.name = request.name;
        entity.academicYear = ay;
        entity.startDate = request.startDate;
        entity.endDate = request.endDate;
        repository.persist(entity);
        return new SemesterResponse(entity);
    }

    @Transactional
    public SemesterResponse update(Long id, SemesterRequest request) {
        Semester entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        AcademicYear ay = academicYearRepository.findByIdOptional(request.academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYear not found"));
        
        entity.name = request.name;
        entity.academicYear = ay;
        entity.startDate = request.startDate;
        entity.endDate = request.endDate;
        return new SemesterResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Semester entity = repository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
        repository.delete(entity);
    }
}
