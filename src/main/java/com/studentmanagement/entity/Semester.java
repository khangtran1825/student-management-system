package com.studentmanagement.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "semesters", uniqueConstraints = {
        @UniqueConstraint(name = "uq_semester_year", columnNames = {"name", "academic_year_id"})
})
public class Semester extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false, length = 50)
    public String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academic_year_id", nullable = false)
    public AcademicYear academicYear;

    @NotNull
    @Column(name = "start_date", nullable = false)
    public LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    public LocalDate endDate;

    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

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
