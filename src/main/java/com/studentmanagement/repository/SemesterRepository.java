package com.studentmanagement.repository;

import com.studentmanagement.entity.Semester;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SemesterRepository implements PanacheRepository<Semester> {
}
