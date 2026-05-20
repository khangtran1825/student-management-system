-- V2__add_semesters.sql
-- Migration v2: add academic_years, semesters, schedules, exams, attendances and alter scores

-- TABLE: academic_years
CREATE TABLE IF NOT EXISTS academic_years (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLE: semesters
CREATE TABLE IF NOT EXISTS semesters (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(50)  NOT NULL,
    academic_year_id BIGINT       NOT NULL,
    start_date       DATE         NOT NULL,
    end_date         DATE         NOT NULL,
    created_at       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_semester_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years(id) ON DELETE CASCADE,
    CONSTRAINT uq_semester_year UNIQUE (name, academic_year_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLE: schedules
CREATE TABLE IF NOT EXISTS schedules (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id     BIGINT       NOT NULL,
    subject_id   BIGINT       NOT NULL,
    teacher_name VARCHAR(100) NOT NULL,
    room         VARCHAR(50)  NOT NULL,
    day_of_week  ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
    start_time   TIME         NOT NULL,
    end_time     TIME         NOT NULL,
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedule_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    CONSTRAINT fk_schedule_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLE: exams
CREATE TABLE IF NOT EXISTS exams (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id   BIGINT       NOT NULL,
    semester_id  BIGINT       NOT NULL,
    exam_date    DATETIME     NOT NULL,
    room         VARCHAR(50)  NOT NULL,
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_exam_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    CONSTRAINT fk_exam_semester FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLE: attendances
CREATE TABLE IF NOT EXISTS attendances (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id   BIGINT       NOT NULL,
    schedule_id  BIGINT       NOT NULL,
    date         DATE         NOT NULL,
    status       ENUM('PRESENT', 'ABSENT', 'EXCUSED') NOT NULL,
    note         VARCHAR(255),
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE,
    CONSTRAINT uq_attendance_student_schedule_date UNIQUE (student_id, schedule_id, date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ALTER scores: add semester_id column (nullable) and unique constraint change
ALTER TABLE scores ADD COLUMN semester_id BIGINT NULL;
ALTER TABLE scores ADD CONSTRAINT fk_score_semester FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE SET NULL;
-- Drop and recreate unique constraint to include semester
DROP INDEX uq_student_subject ON scores;
CREATE UNIQUE INDEX uq_student_subject_semester ON scores(student_id, subject_id, semester_id);

-- INDEXES
CREATE INDEX idx_semester_year     ON semesters(academic_year_id);
CREATE INDEX idx_schedule_class    ON schedules(class_id);
CREATE INDEX idx_schedule_subject  ON schedules(subject_id);
CREATE INDEX idx_exam_subject      ON exams(subject_id);
CREATE INDEX idx_exam_semester     ON exams(semester_id);
CREATE INDEX idx_attendance_student ON attendances(student_id);
CREATE INDEX idx_attendance_schedule ON attendances(schedule_id);
