package com.studentmanagement.dto.response;

import com.studentmanagement.entity.Score;
import com.studentmanagement.entity.Student;
import java.math.BigDecimal;

public class ClassScoreResponse {
    public Long studentId;
    public String studentCode;
    public String fullName;

    public Long scoreId;
    public BigDecimal midtermScore;
    public BigDecimal finalScore;
    public BigDecimal averageScore;
    public String grade;

    public ClassScoreResponse(Student student, Score score, String gradeCalc) {
        this.studentId = student.id;
        this.studentCode = student.studentCode;
        this.fullName = student.fullName;

        if (score != null) {
            this.scoreId = score.id;
            this.midtermScore = score.midtermScore;
            this.finalScore = score.finalScore;
            this.averageScore = score.averageScore;
            this.grade = gradeCalc;
        } else {
            this.scoreId = null;
            this.midtermScore = null;
            this.finalScore = null;
            this.averageScore = null;
            this.grade = null;
        }
    }
}
