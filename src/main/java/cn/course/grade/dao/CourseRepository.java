package cn.course.grade.dao;

import cn.course.grade.model.Course;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    public List<Course> list(String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder("select course_id,course_code,course_name,credit,teacher_name from course_catalog where 1=1");
        List<Object> args = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" and (course_code like ? or course_name like ? or teacher_name like ?)");
            String like = "%" + keyword.trim() + "%";
            args.add(like);
            args.add(like);
            args.add(like);
        }
        sql.append(" order by course_code");
        return SqlRows.list(sql.toString(), this::map, args.toArray());
    }

    public Course byId(int id) throws SQLException {
        return SqlRows.one("select course_id,course_code,course_name,credit,teacher_name from course_catalog where course_id=?", this::map, id);
    }

    public boolean codeTaken(String code, Integer exceptId) throws SQLException {
        String sql = "select course_id from course_catalog where course_code=?" + (exceptId == null ? "" : " and course_id<>?");
        Object[] args = exceptId == null ? new Object[]{code} : new Object[]{code, exceptId};
        return SqlRows.one(sql, rs -> rs.getInt(1), args) != null;
    }

    public void save(Course course) throws SQLException {
        SqlRows.change("insert into course_catalog(course_code,course_name,credit,teacher_name) values(?,?,?,?)",
                course.getCourseCode(), course.getCourseName(), course.getCredit(), course.getTeacherName());
    }

    public void update(Course course) throws SQLException {
        SqlRows.change("update course_catalog set course_code=?, course_name=?, credit=?, teacher_name=? where course_id=?",
                course.getCourseCode(), course.getCourseName(), course.getCredit(), course.getTeacherName(), course.getCourseId());
    }

    public void delete(int id) throws SQLException {
        SqlRows.change("delete from course_catalog where course_id=?", id);
    }

    public int count() throws SQLException {
        Integer count = SqlRows.one("select count(*) from course_catalog", rs -> rs.getInt(1));
        return count == null ? 0 : count;
    }

    private Course map(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        BigDecimal credit = rs.getBigDecimal("credit");
        course.setCredit(credit);
        course.setTeacherName(rs.getString("teacher_name"));
        return course;
    }
}
