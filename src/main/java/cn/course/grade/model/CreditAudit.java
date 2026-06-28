package cn.course.grade.model;

import java.math.BigDecimal;

public class CreditAudit {
    private String studentNo;
    private String fullName;
    private BigDecimal passedCredit;
    private BigDecimal failedCredit;
    private BigDecimal weightedPoint;
    private Integer completedCourses;

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

    public BigDecimal getPassedCredit() {
        return passedCredit;
    }

    public void setPassedCredit(BigDecimal passedCredit) {
        this.passedCredit = passedCredit;
    }

    public BigDecimal getFailedCredit() {
        return failedCredit;
    }

    public void setFailedCredit(BigDecimal failedCredit) {
        this.failedCredit = failedCredit;
    }

    public BigDecimal getWeightedPoint() {
        return weightedPoint;
    }

    public void setWeightedPoint(BigDecimal weightedPoint) {
        this.weightedPoint = weightedPoint;
    }

    public Integer getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(Integer completedCourses) {
        this.completedCourses = completedCourses;
    }
}
