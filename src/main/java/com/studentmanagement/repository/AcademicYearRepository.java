package com.studentmanagement.repository;

import com.studentmanagement.entity.AcademicYear;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcademicYearRepository implements PanacheRepository<AcademicYear> {
}
