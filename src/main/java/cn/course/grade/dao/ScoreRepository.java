package cn.course.grade.dao;

import cn.course.grade.model.CourseAverage;
import cn.course.grade.model.CreditAudit;
import cn.course.grade.model.ProgramProgress;
import cn.course.grade.model.ScholarshipCandidate;
import cn.course.grade.model.Score;
import cn.course.grade.model.ScoreBand;
import cn.course.grade.model.StudyAlert;
import cn.course.grade.model.StudentRank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreRepository {
    private static final String SCORE_COLUMNS =
            "select sc.score_id,sc.student_id,sc.course_id,sp.student_no,sp.full_name,cc.course_name," +
                    "sc.term_label,sc.score_value,sc.remark,sc.created_at " +
                    "from score_record sc join student_profile sp on sc.student_id=sp.student_id " +
                    "join course_catalog cc on sc.course_id=cc.course_id ";

    public List<Score> list(String term, String keyword, Integer accountId) throws SQLException {
        StringBuilder sql = new StringBuilder(SCORE_COLUMNS + "where 1=1");
        List<Object> args = new ArrayList<>();
        if (term != null && !term.trim().isEmpty()) {
            sql.append(" and sc.term_label=?");
            args.add(term.trim());
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" and (sp.student_no like ? or sp.full_name like ? or cc.course_name like ?)");
            String like = "%" + keyword.trim() + "%";
            args.add(like);
            args.add(like);
            args.add(like);
        }
        if (accountId != null) {
            sql.append(" and sp.account_id=?");
            args.add(accountId);
        }
        sql.append(" order by sc.created_at desc, sc.score_id desc");
        return SqlRows.list(sql.toString(), this::map, args.toArray());
    }

    public Score byId(int id) throws SQLException {
        return SqlRows.one(SCORE_COLUMNS + "where sc.score_id=?", this::map, id);
    }

    public boolean duplicated(Integer studentId, Integer courseId, String term, Integer exceptScoreId) throws SQLException {
        String sql = "select score_id from score_record where student_id=? and course_id=? and term_label=?" +
                (exceptScoreId == null ? "" : " and score_id<>?");
        Object[] args = exceptScoreId == null
                ? new Object[]{studentId, courseId, term}
                : new Object[]{studentId, courseId, term, exceptScoreId};
        return SqlRows.one(sql, rs -> rs.getInt(1), args) != null;
    }

    public void save(Score score) throws SQLException {
        SqlRows.change("insert into score_record(student_id,course_id,term_label,score_value,remark,created_at) values(?,?,?,?,?,now())",
                score.getStudentId(), score.getCourseId(), score.getTermLabel(), score.getScoreValue(), score.getRemark());
    }

    public void update(Score score) throws SQLException {
        SqlRows.change("update score_record set student_id=?, course_id=?, term_label=?, score_value=?, remark=? where score_id=?",
                score.getStudentId(), score.getCourseId(), score.getTermLabel(), score.getScoreValue(), score.getRemark(), score.getScoreId());
    }

    public void delete(int id) throws SQLException {
        SqlRows.change("delete from score_record where score_id=?", id);
    }

    public int count() throws SQLException {
        Integer count = SqlRows.one("select count(*) from score_record", rs -> rs.getInt(1));
        return count == null ? 0 : count;
    }

    public BigDecimal average(Integer accountId) throws SQLException {
        String sql = "select coalesce(avg(sc.score_value),0) from score_record sc";
        Object[] args = new Object[0];
        if (accountId != null) {
            sql += " join student_profile sp on sc.student_id=sp.student_id where sp.account_id=?";
            args = new Object[]{accountId};
        }
        BigDecimal avg = SqlRows.one(sql, rs -> rs.getBigDecimal(1), args);
        return avg == null ? BigDecimal.ZERO : avg;
    }

    public List<CourseAverage> courseAverages(Integer accountId) throws SQLException {
        StringBuilder sql = new StringBuilder("select cc.course_name, round(avg(sc.score_value),2) avg_score " +
                "from score_record sc join course_catalog cc on sc.course_id=cc.course_id ");
        List<Object> args = new ArrayList<>();
        if (accountId != null) {
            sql.append("join student_profile sp on sc.student_id=sp.student_id where sp.account_id=? ");
            args.add(accountId);
        }
        sql.append("group by cc.course_name order by cc.course_name");
        return SqlRows.list(sql.toString(), rs -> {
            CourseAverage item = new CourseAverage();
            item.setCourseName(rs.getString("course_name"));
            item.setAverageScore(rs.getBigDecimal("avg_score"));
            return item;
        }, args.toArray());
    }

    public List<ScoreBand> bands(String term) throws SQLException {
        String sql = "select band_name, count(*) amount from (" +
                "select case when score_value>=90 then '90-100' when score_value>=80 then '80-89' " +
                "when score_value>=70 then '70-79' when score_value>=60 then '60-69' else '0-59' end band_name " +
                "from score_record where (? is null or term_label=?)) t group by band_name " +
                "order by field(band_name,'90-100','80-89','70-79','60-69','0-59')";
        String cleanTerm = term == null || term.trim().isEmpty() ? null : term.trim();
        return SqlRows.list(sql, rs -> {
            ScoreBand band = new ScoreBand();
            band.setBandName(rs.getString("band_name"));
            band.setAmount(rs.getInt("amount"));
            return band;
        }, cleanTerm, cleanTerm);
    }

    public List<StudentRank> ranking(String term) throws SQLException {
        String sql = "select sp.student_no, sp.full_name, round(avg(sc.score_value),2) avg_score " +
                "from score_record sc join student_profile sp on sc.student_id=sp.student_id " +
                "where (? is null or sc.term_label=?) group by sp.student_no, sp.full_name " +
                "order by avg_score desc, sp.student_no limit 20";
        String cleanTerm = term == null || term.trim().isEmpty() ? null : term.trim();
        return SqlRows.list(sql, rs -> {
            StudentRank rank = new StudentRank();
            rank.setStudentNo(rs.getString("student_no"));
            rank.setFullName(rs.getString("full_name"));
            rank.setAverageScore(rs.getBigDecimal("avg_score"));
            return rank;
        }, cleanTerm, cleanTerm);
    }

    public List<StudyAlert> alerts(String term, Integer accountId) throws SQLException {
        StringBuilder sql = new StringBuilder("select sp.student_no,sp.full_name,cc.course_name,sc.term_label,sc.score_value," +
                "case when sc.score_value<60 then '不及格' else '临界风险' end alert_type " +
                "from score_record sc join student_profile sp on sc.student_id=sp.student_id " +
                "join course_catalog cc on sc.course_id=cc.course_id where sc.score_value<70");
        List<Object> args = new ArrayList<>();
        if (term != null && !term.trim().isEmpty()) {
            sql.append(" and sc.term_label=?");
            args.add(term.trim());
        }
        if (accountId != null) {
            sql.append(" and sp.account_id=?");
            args.add(accountId);
        }
        sql.append(" order by sc.score_value, sp.student_no");
        return SqlRows.list(sql.toString(), rs -> {
            StudyAlert alert = new StudyAlert();
            alert.setStudentNo(rs.getString("student_no"));
            alert.setStudentName(rs.getString("full_name"));
            alert.setCourseName(rs.getString("course_name"));
            alert.setTermLabel(rs.getString("term_label"));
            alert.setScoreValue(rs.getBigDecimal("score_value"));
            alert.setAlertType(rs.getString("alert_type"));
            alert.setAdvice(rs.getBigDecimal("score_value").intValue() < 60 ? "安排补考并跟进基础知识" : "建议参加课程答疑并完成错题复盘");
            return alert;
        }, args.toArray());
    }

    public List<ScholarshipCandidate> scholarship(String term, BigDecimal threshold) throws SQLException {
        String cleanTerm = term == null || term.trim().isEmpty() ? null : term.trim();
        BigDecimal min = threshold == null ? BigDecimal.valueOf(85) : threshold;
        String sql = "select sp.student_no,sp.full_name,sp.class_name,round(avg(sc.score_value),2) avg_score," +
                "sum(case when sc.score_value>=90 then 1 else 0 end) excellent_courses," +
                "sum(case when sc.score_value<60 then 1 else 0 end) failed_courses " +
                "from score_record sc join student_profile sp on sc.student_id=sp.student_id " +
                "where (? is null or sc.term_label=?) group by sp.student_no,sp.full_name,sp.class_name " +
                "having avg_score>=? and failed_courses=0 order by avg_score desc, excellent_courses desc";
        return SqlRows.list(sql, rs -> {
            ScholarshipCandidate item = new ScholarshipCandidate();
            item.setStudentNo(rs.getString("student_no"));
            item.setFullName(rs.getString("full_name"));
            item.setClassName(rs.getString("class_name"));
            item.setAverageScore(rs.getBigDecimal("avg_score"));
            item.setExcellentCourses(rs.getInt("excellent_courses"));
            item.setFailedCourses(rs.getInt("failed_courses"));
            item.setEligibility(rs.getBigDecimal("avg_score").compareTo(BigDecimal.valueOf(90)) >= 0 ? "一等奖候选" : "二等奖候选");
            return item;
        }, cleanTerm, cleanTerm, min);
    }

    public List<CreditAudit> creditAudit(String term, Integer accountId) throws SQLException {
        StringBuilder sql = new StringBuilder("select sp.student_no,sp.full_name," +
                "coalesce(sum(case when sc.score_value>=60 then cc.credit else 0 end),0) passed_credit," +
                "coalesce(sum(case when sc.score_value<60 then cc.credit else 0 end),0) failed_credit," +
                "round(coalesce(sum(case when sc.score_value>=60 then ((sc.score_value-50)/10)*cc.credit else 0 end)/nullif(sum(cc.credit),0),0),2) weighted_point," +
                "count(*) completed_courses from score_record sc join student_profile sp on sc.student_id=sp.student_id " +
                "join course_catalog cc on sc.course_id=cc.course_id where 1=1");
        List<Object> args = new ArrayList<>();
        if (term != null && !term.trim().isEmpty()) {
            sql.append(" and sc.term_label=?");
            args.add(term.trim());
        }
        if (accountId != null) {
            sql.append(" and sp.account_id=?");
            args.add(accountId);
        }
        sql.append(" group by sp.student_no,sp.full_name order by sp.student_no");
        return SqlRows.list(sql.toString(), rs -> {
            CreditAudit audit = new CreditAudit();
            audit.setStudentNo(rs.getString("student_no"));
            audit.setFullName(rs.getString("full_name"));
            audit.setPassedCredit(rs.getBigDecimal("passed_credit"));
            audit.setFailedCredit(rs.getBigDecimal("failed_credit"));
            audit.setWeightedPoint(rs.getBigDecimal("weighted_point"));
            audit.setCompletedCourses(rs.getInt("completed_courses"));
            return audit;
        }, args.toArray());
    }

    public List<ProgramProgress> programProgress(BigDecimal requiredCredit) throws SQLException {
        BigDecimal target = requiredCredit == null ? BigDecimal.valueOf(12) : requiredCredit;
        String sql = "select sp.class_name,count(distinct sp.student_id) student_total," +
                "round(avg(coalesce(t.passed_credit,0)),2) avg_passed_credit " +
                "from student_profile sp left join (" +
                "select sc.student_id,sum(case when sc.score_value>=60 then cc.credit else 0 end) passed_credit " +
                "from score_record sc join course_catalog cc on sc.course_id=cc.course_id group by sc.student_id" +
                ") t on sp.student_id=t.student_id where sp.archived=0 group by sp.class_name order by sp.class_name";
        return SqlRows.list(sql, rs -> {
            ProgramProgress progress = new ProgramProgress();
            BigDecimal avg = rs.getBigDecimal("avg_passed_credit");
            progress.setClassName(rs.getString("class_name"));
            progress.setStudentTotal(rs.getInt("student_total"));
            progress.setRequiredCredit(target);
            progress.setAveragePassedCredit(avg);
            progress.setCompletionRate(avg.multiply(BigDecimal.valueOf(100)).divide(target, 2, RoundingMode.HALF_UP));
            progress.setCoachingFocus(avg.compareTo(target.multiply(new BigDecimal("0.8"))) >= 0 ? "按计划推进" : "需要补修学分跟进");
            return progress;
        });
    }

    private Score map(ResultSet rs) throws SQLException {
        Score score = new Score();
        score.setScoreId(rs.getInt("score_id"));
        score.setStudentId(rs.getInt("student_id"));
        score.setCourseId(rs.getInt("course_id"));
        score.setStudentNo(rs.getString("student_no"));
        score.setStudentName(rs.getString("full_name"));
        score.setCourseName(rs.getString("course_name"));
        score.setTermLabel(rs.getString("term_label"));
        score.setScoreValue(rs.getBigDecimal("score_value"));
        score.setRemark(rs.getString("remark"));
        if (rs.getTimestamp("created_at") != null) {
            score.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return score;
    }
}
