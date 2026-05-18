package com.studentmanagement.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "subject_code", nullable = false, unique = true, length = 20)
    public String subjectCode;

    @NotBlank
    @Size(max = 150)
    @Column(name = "subject_name", nullable = false, length = 150)
    public String subjectName;

    @NotNull
    @Min(1) @Max(10)
    @Column(name = "credits", nullable = false)
    public Integer credits;

    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    public List<Score> scores;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
