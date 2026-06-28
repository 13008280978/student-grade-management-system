package cn.course.grade.model;

import java.math.BigDecimal;

public class CourseAverage {
    private String courseName;
    private BigDecimal averageScore;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }
}
