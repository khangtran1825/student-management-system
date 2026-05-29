package com.studentmanagement.repository;

import com.studentmanagement.entity.Teacher;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TeacherRepository implements PanacheRepository<Teacher> {

    public List<Teacher> findByNameOrCode(String keyword, int page, int size) {
        String query = "lower(fullName) like :keyword or lower(teacherCode) like :keyword";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";

        return find(query, Parameters.with("keyword", likeKeyword))
                .page(Page.of(page, size))
                .list();
    }

    public long countByNameOrCode(String keyword) {
        String query = "lower(fullName) like :keyword or lower(teacherCode) like :keyword";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";

        return count(query, Parameters.with("keyword", likeKeyword));
    }

    public Teacher findByTeacherCode(String code) {
        return find("teacherCode", code).firstResult();
    }
}
