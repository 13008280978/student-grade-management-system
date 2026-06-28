package cn.course.grade.model;

import java.math.BigDecimal;

public class ScholarshipCandidate {
    private String studentNo;
    private String fullName;
    private String className;
    private BigDecimal averageScore;
    private Integer excellentCourses;
    private Integer failedCourses;
    private String eligibility;

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getExcellentCourses() {
        return excellentCourses;
    }

    public void setExcellentCourses(Integer excellentCourses) {
        this.excellentCourses = excellentCourses;
    }

    public Integer getFailedCourses() {
        return failedCourses;
    }

    public void setFailedCourses(Integer failedCourses) {
        this.failedCourses = failedCourses;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }
}
