-- V3__create_majors_and_link.sql
-- Create majors table and link existing classes to majors

CREATE TABLE IF NOT EXISTS majors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NULL,
    name VARCHAR(150) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert distinct major names from classes into majors (skip empty/null)
INSERT IGNORE INTO majors (name)
SELECT DISTINCT TRIM(major) FROM classes WHERE major IS NOT NULL AND TRIM(major) <> '';

-- Add major_id to classes
ALTER TABLE classes ADD COLUMN major_id BIGINT NULL;

-- Link classes to majors by matching the name
UPDATE classes c
JOIN majors m ON TRIM(c.major) = m.name
SET c.major_id = m.id;

-- Add FK constraint if not exists
ALTER TABLE classes
  ADD CONSTRAINT fk_class_major FOREIGN KEY (major_id) REFERENCES majors(id);

-- Note: we intentionally keep the text column `classes.major` until you confirm it's safe to drop.
