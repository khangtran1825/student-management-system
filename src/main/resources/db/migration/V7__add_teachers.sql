-- V7__add_teachers.sql

CREATE TABLE IF NOT EXISTS teachers (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_code  VARCHAR(20)  NOT NULL UNIQUE,
    full_name     VARCHAR(100) NOT NULL,
    gender        ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    date_of_birth DATE         NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone         VARCHAR(15),
    department    VARCHAR(150),
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE users ADD COLUMN teacher_id BIGINT NULL;
ALTER TABLE users ADD CONSTRAINT fk_user_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL;

-- Migrate existing TEACHER users to teachers table
INSERT INTO teachers (teacher_code, full_name, gender, date_of_birth, email)
SELECT 
    CONCAT('TC', LPAD(id, 4, '0')), 
    username, 
    'OTHER', 
    '1980-01-01', 
    email 
FROM users WHERE role = 'TEACHER';

-- Update users table to link to teachers
UPDATE users u
JOIN teachers t ON t.email = u.email
SET u.teacher_id = t.id
WHERE u.role = 'TEACHER';

-- Drop the old foreign key from schedules
ALTER TABLE schedules DROP FOREIGN KEY fk_schedule_teacher;

-- Update schedules.teacher_id from User.id to Teacher.id
UPDATE schedules s
JOIN users u ON s.teacher_id = u.id
SET s.teacher_id = u.teacher_id
WHERE u.teacher_id IS NOT NULL;

-- Add new foreign key to schedules pointing to teachers
ALTER TABLE schedules ADD CONSTRAINT fk_schedule_teacher_new FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE RESTRICT;
