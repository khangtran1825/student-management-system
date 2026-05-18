package com.studentmanagement.repository;

import com.studentmanagement.entity.ClassEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClassRepository implements PanacheRepository<ClassEntity> {
}