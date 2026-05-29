package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Exam;
import java.time.LocalDateTime;
import java.util.List;

public class ExamResponse {
    public Long id;
    public SubjectResponse subject;
    public SemesterResponse semester;
    public LocalDateTime examDate;
    public String room;
    public List<String> classNames;

    public ExamResponse(Exam entity) {
        this.id = entity.id;
        if (entity.subject != null) {
            this.subject = new SubjectResponse(entity.subject);
        }
        if (entity.semester != null) {
            this.semester = new SemesterResponse(entity.semester);
        }
        this.examDate = entity.examDate;
        this.room = entity.room;
    }

    public ExamResponse(Exam entity, List<String> classNames) {
        this(entity);
        this.classNames = classNames;
    }
}
