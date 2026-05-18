-- ==========================================
-- SEED DATA - Loaded by Hibernate on startup
-- ==========================================

-- Classes
INSERT INTO classes (class_code, class_name, major) VALUES
('CNTT01', 'Công nghệ thông tin K1', 'Công nghệ thông tin'),
('CNTT02', 'Công nghệ thông tin K2', 'Công nghệ thông tin'),
('KTPM01', 'Kỹ thuật phần mềm K1', 'Kỹ thuật phần mềm'),
('HTTT01', 'Hệ thống thông tin K1', 'Hệ thống thông tin'),
('MMT01',  'Mạng máy tính K1',      'Mạng máy tính');

-- Students
INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id) VALUES
('SV001', 'Nguyễn Văn An',    'MALE',   '2002-03-15', 'nguyenvanan@gmail.com',    '0901234501', 'Hà Nội',        1),
('SV002', 'Trần Thị Bình',    'FEMALE', '2002-07-22', 'tranthiminh@gmail.com',    '0901234502', 'Hồ Chí Minh',   1),
('SV003', 'Lê Văn Cường',     'MALE',   '2001-11-08', 'levancuong@gmail.com',     '0901234503', 'Đà Nẵng',       1),
('SV004', 'Phạm Thị Dung',    'FEMALE', '2002-05-30', 'phamthidung@gmail.com',    '0901234504', 'Cần Thơ',       2),
('SV005', 'Hoàng Văn Em',     'MALE',   '2001-09-12', 'hoangvanem@gmail.com',     '0901234505', 'Huế',           2),
('SV006', 'Vũ Thị Phương',    'FEMALE', '2002-01-25', 'vuthiphuong@gmail.com',    '0901234506', 'Hải Phòng',     3),
('SV007', 'Đặng Văn Giang',   'MALE',   '2001-06-17', 'dangvangiang@gmail.com',   '0901234507', 'Nha Trang',     3),
('SV008', 'Bùi Thị Hoa',      'FEMALE', '2002-08-03', 'buithihoa@gmail.com',      '0901234508', 'Vũng Tàu',      4),
('SV009', 'Ngô Văn Inh',      'MALE',   '2001-12-20', 'ngovaninh@gmail.com',      '0901234509', 'Bình Dương',    4),
('SV010', 'Lý Thị Kim',       'FEMALE', '2002-04-11', 'lythikim@gmail.com',       '0901234510', 'Đồng Nai',      5);

-- Subjects
INSERT INTO subjects (subject_code, subject_name, credits) VALUES
('INT101', 'Nhập môn lập trình',           3),
('INT102', 'Lập trình hướng đối tượng',    3),
('INT201', 'Cấu trúc dữ liệu và giải thuật', 4),
('INT202', 'Cơ sở dữ liệu',               3),
('INT301', 'Lập trình Web',               3),
('INT302', 'Lập trình di động',            3),
('INT401', 'Trí tuệ nhân tạo',            4),
('MAT101', 'Toán cao cấp',               4),
('MAT102', 'Xác suất thống kê',          3),
('ENG101', 'Tiếng Anh chuyên ngành',     3);

-- Scores
INSERT INTO scores (student_id, subject_id, midterm_score, final_score) VALUES
(1, 1, 7.5,  8.0),
(1, 2, 6.5,  7.0),
(1, 3, 8.0,  8.5),
(2, 1, 9.0,  8.5),
(2, 2, 7.0,  7.5),
(2, 4, 8.5,  9.0),
(3, 1, 6.0,  6.5),
(3, 3, 7.5,  7.0),
(3, 5, 8.0,  8.5),
(4, 2, 9.5,  9.0),
(4, 4, 8.0,  8.5),
(5, 1, 5.5,  6.0),
(5, 2, 7.0,  7.5),
(6, 3, 8.5,  9.0),
(6, 4, 7.5,  8.0),
(7, 5, 9.0,  9.5),
(8, 1, 6.5,  7.0),
(8, 6, 8.0,  8.5),
(9, 2, 7.5,  8.0),
(9, 7, 8.5,  9.0),
(10, 1, 9.0, 9.5),
(10, 8, 7.0, 7.5);

-- Users (passwords are BCrypt hashed - plain: admin123, teacher123, student123)
INSERT INTO users (username, password, role, student_id) VALUES
('admin',    '$2a$12$LQV3c1yqBWVHxkd0LHAkCOYz6TtxMQyCFfcbH6wyxfKuBbJxVJJmW', 'ADMIN',   NULL),
('teacher1', '$2a$12$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWu', 'TEACHER', NULL),
('sv001',    '$2a$12$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWu', 'STUDENT', 1),
('sv002',    '$2a$12$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWu', 'STUDENT', 2);
-- Passwords: admin -> admin123 | teacher1 -> teacher123 | sv001,sv002 -> student123
