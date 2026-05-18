package com.studentmanagement.repository;

import com.studentmanagement.entity.ClassEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ClassRepository implements PanacheRepository<ClassEntity> {

    public List<Object[]> findAllWithStudentCount() {
        return getEntityManager()
                .createQuery(
                        "SELECT c, COUNT(s.id) FROM ClassEntity c " +
                                "LEFT JOIN c.students s " +
                                "GROUP BY c.id " +
                                "ORDER BY c.id ASC",
                        Object[].class)
                .getResultList();
    }

    public boolean existsByClassCode(String classCode) {
        return count("classCode", classCode) > 0;
    }

    public boolean existsByClassCodeAndIdNot(String classCode, Long id) {
        return count("classCode = ?1 and id != ?2", classCode, id) > 0;
    }
}