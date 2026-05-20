package com.studentmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "classes")
public class ClassEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "class_code", nullable = false, unique = true, length = 20)
    public String classCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "class_name", nullable = false, length = 100)
    public String className;

    @NotBlank
    @Size(max = 100)
    @Column(name = "major", nullable = false, length = 100)
    public String major;

    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<Student> students;

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
