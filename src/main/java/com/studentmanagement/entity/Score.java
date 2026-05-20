package com.studentmanagement.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "scores",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_student_subject_semester",
                columnNames = {"student_id", "subject_id", "semester_id"}
        )
)
public class Score extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    public Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    public Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id")
    public Semester semester;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(name = "midterm_score", nullable = false, precision = 4, scale = 2)
    public BigDecimal midtermScore;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(name = "final_score", nullable = false, precision = 4, scale = 2)
    public BigDecimal finalScore;

    @Column(name = "average_score", precision = 4, scale = 2, insertable = false, updatable = false)
    public BigDecimal averageScore;

    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateAverage();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateAverage();
    }

    public void calculateAverage() {
        if (midtermScore != null && finalScore != null) {
            averageScore = midtermScore.multiply(new BigDecimal("0.4"))
                    .add(finalScore.multiply(new BigDecimal("0.6")))
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }
}
