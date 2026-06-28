package cn.course.grade.model;

import java.math.BigDecimal;

public class ProgramProgress {
    private String className;
    private Integer studentTotal;
    private BigDecimal requiredCredit;
    private BigDecimal averagePassedCredit;
    private BigDecimal completionRate;
    private String coachingFocus;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getStudentTotal() {
        return studentTotal;
    }

    public void setStudentTotal(Integer studentTotal) {
        this.studentTotal = studentTotal;
    }

    public BigDecimal getRequiredCredit() {
        return requiredCredit;
    }

    public void setRequiredCredit(BigDecimal requiredCredit) {
        this.requiredCredit = requiredCredit;
    }

    public BigDecimal getAveragePassedCredit() {
        return averagePassedCredit;
    }

    public void setAveragePassedCredit(BigDecimal averagePassedCredit) {
        this.averagePassedCredit = averagePassedCredit;
    }

    public BigDecimal getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(BigDecimal completionRate) {
        this.completionRate = completionRate;
    }

    public String getCoachingFocus() {
        return coachingFocus;
    }

    public void setCoachingFocus(String coachingFocus) {
        this.coachingFocus = coachingFocus;
    }
}
