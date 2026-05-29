-- V5__add_teacher_id_to_schedules.sql
-- Add teacher_id to schedules and backfill from existing teacher_name values when possible.

ALTER TABLE schedules ADD COLUMN teacher_id BIGINT NULL AFTER subject_id;
ALTER TABLE schedules ADD CONSTRAINT fk_schedule_teacher FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE RESTRICT;

UPDATE schedules s
JOIN users u ON u.username = s.teacher_name AND u.role = 'TEACHER'
SET s.teacher_id = u.id
WHERE s.teacher_id IS NULL;

UPDATE schedules
SET teacher_id = (
    SELECT id FROM users WHERE role = 'TEACHER' ORDER BY id LIMIT 1
)
WHERE teacher_id IS NULL;

ALTER TABLE schedules MODIFY teacher_id BIGINT NOT NULL;
