// File: src/main/java/com/studentmanagement/repository/StudentRepository.java
package com.studentmanagement.repository;

import com.studentmanagement.entity.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class StudentRepository implements PanacheRepository<Student> {

    public List<Student> findByNameOrCode(String keyword, int page, int size) {
        String query = "lower(fullName) like :keyword or lower(studentCode) like :keyword";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";

        return find(query, Parameters.with("keyword", likeKeyword))
                .page(Page.of(page, size))
                .list();
    }

    public List<Student> findByNameOrCodeAndClassId(String keyword, Long classId, int page, int size) {
        String query = "(lower(fullName) like :keyword or lower(studentCode) like :keyword) and classEntity.id = :classId";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";

        return find(query, Parameters.with("keyword", likeKeyword).and("classId", classId))
                .page(Page.of(page, size))
                .list();
    }

    public long countByNameOrCodeAndClassId(String keyword, Long classId) {
        String query = "(lower(fullName) like :keyword or lower(studentCode) like :keyword) and classEntity.id = :classId";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";

        return count(query, Parameters.with("keyword", likeKeyword).and("classId", classId));
    }

    public long countByNameOrCode(String keyword) {
        String query = "lower(fullName) like :keyword or lower(studentCode) like :keyword";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";

        return count(query, Parameters.with("keyword", likeKeyword));
    }

    public Student findByStudentCode(String code) {
        return find("studentCode", code).firstResult();
    }
}