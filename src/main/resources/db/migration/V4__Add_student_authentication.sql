-- V4: Add student authentication (username, password)
ALTER TABLE students 
ADD COLUMN username VARCHAR(50) UNIQUE COMMENT 'Student username for login',
ADD COLUMN password VARCHAR(255) COMMENT 'Student password (bcrypt hashed)';

-- Index for username lookup
CREATE INDEX idx_student_username ON students(username);
