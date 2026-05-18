-- ==========================================
-- STUDENT MANAGEMENT SYSTEM - DATABASE SCHEMA
-- MySQL 8.0+
-- ==========================================

CREATE DATABASE IF NOT EXISTS student_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE student_management;

-- ==========================================
-- TABLE: classes
-- ==========================================
CREATE TABLE IF NOT EXISTS classes (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_code  VARCHAR(20)  NOT NULL UNIQUE,
    class_name  VARCHAR(100) NOT NULL,
    major       VARCHAR(100) NOT NULL,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- TABLE: students
-- ==========================================
CREATE TABLE IF NOT EXISTS students (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_code  VARCHAR(20)  NOT NULL UNIQUE,
    full_name     VARCHAR(100) NOT NULL,
    gender        ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    date_of_birth DATE         NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone         VARCHAR(15),
    address       VARCHAR(255),
    class_id      BIGINT       NOT NULL,
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- TABLE: subjects
-- ==========================================
CREATE TABLE IF NOT EXISTS subjects (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_code  VARCHAR(20)  NOT NULL UNIQUE,
    subject_name  VARCHAR(150) NOT NULL,
    credits       INT          NOT NULL CHECK (credits BETWEEN 1 AND 10),
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- TABLE: scores
-- ==========================================
CREATE TABLE IF NOT EXISTS scores (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id     BIGINT         NOT NULL,
    subject_id     BIGINT         NOT NULL,
    midterm_score  DECIMAL(4, 2)  NOT NULL CHECK (midterm_score BETWEEN 0 AND 10),
    final_score    DECIMAL(4, 2)  NOT NULL CHECK (final_score BETWEEN 0 AND 10),
    average_score  DECIMAL(4, 2)  GENERATED ALWAYS AS (ROUND(midterm_score * 0.4 + final_score * 0.6, 2)) STORED,
    created_at     DATETIME       DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_score_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_score_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE RESTRICT,
    CONSTRAINT uq_student_subject UNIQUE (student_id, subject_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- TABLE: users (for authentication)
-- ==========================================
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL DEFAULT 'STUDENT',
    student_id  BIGINT       NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- INDEXES
-- ==========================================
CREATE INDEX idx_student_name      ON students(full_name);
CREATE INDEX idx_student_code      ON students(student_code);
CREATE INDEX idx_student_class     ON students(class_id);
CREATE INDEX idx_score_student     ON scores(student_id);
CREATE INDEX idx_score_subject     ON scores(subject_id);
CREATE INDEX idx_user_username     ON users(username);
