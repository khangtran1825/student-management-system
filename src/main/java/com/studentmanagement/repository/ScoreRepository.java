package com.studentmanagement.repository;

import com.studentmanagement.entity.Score;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ScoreRepository implements PanacheRepository<Score> {

    public List<Score> findAllPaged(int page, int size) {
        return findAll().page(Page.of(page, size)).list();
    }

    public List<Score> findBySubjectId(Long subjectId) {
        return list("subject.id", subjectId);
    }

    public List<Score> findByStudentId(Long studentId) {
        return list("student.id", studentId);
    }

    public boolean existsByStudentAndSubject(Long studentId, Long subjectId) {
        return count("student.id = :sid and subject.id = :subid",
                Parameters.with("sid", studentId).and("subid", subjectId)) > 0;
    }

    public boolean existsByStudentAndSubjectExcluding(Long studentId, Long subjectId, Long excludeId) {
        return count("student.id = :sid and subject.id = :subid and id != :eid",
                Parameters.with("sid", studentId).and("subid", subjectId).and("eid", excludeId)) > 0;
    }
}