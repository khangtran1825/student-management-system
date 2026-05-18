package com.studentmanagement.repository;

import com.studentmanagement.entity.Subject;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SubjectRepository implements PanacheRepository<Subject> {

    public List<Subject> findByKeyword(String keyword, int page, int size) {
        String like = "%" + keyword.toLowerCase() + "%";
        return find("lower(subjectName) like :kw or lower(subjectCode) like :kw",
                Parameters.with("kw", like))
                .page(Page.of(page, size))
                .list();
    }

    public long countByKeyword(String keyword) {
        String like = "%" + keyword.toLowerCase() + "%";
        return count("lower(subjectName) like :kw or lower(subjectCode) like :kw",
                Parameters.with("kw", like));
    }

    public boolean existsBySubjectCode(String subjectCode) {
        return count("subjectCode", subjectCode) > 0;
    }

    public boolean existsBySubjectCodeAndIdNot(String subjectCode, Long id) {
        return count("subjectCode = ?1 and id != ?2", subjectCode, id) > 0;
    }
}