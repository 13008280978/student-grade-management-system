package cn.course.grade.dao;

import cn.course.grade.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public List<Student> search(String word, Integer ownerAccountId) throws SQLException {
        StringBuilder sql = new StringBuilder("select student_id,account_id,student_no,full_name,gender,class_name,enrollment_year,archived from student_profile where 1=1");
        List<Object> args = new ArrayList<>();
        if (word != null && !word.trim().isEmpty()) {
            sql.append(" and (student_no like ? or full_name like ? or class_name like ?)");
            String like = "%" + word.trim() + "%";
            args.add(like);
            args.add(like);
            args.add(like);
        }
        if (ownerAccountId != null) {
            sql.append(" and account_id=?");
            args.add(ownerAccountId);
        }
        sql.append(" order by student_no");
        return SqlRows.list(sql.toString(), this::map, args.toArray());
    }

    public List<Student> allActive() throws SQLException {
        return SqlRows.list("select student_id,account_id,student_no,full_name,gender,class_name,enrollment_year,archived from student_profile where archived=0 order by student_no", this::map);
    }

    public Student byId(int id) throws SQLException {
        return SqlRows.one("select student_id,account_id,student_no,full_name,gender,class_name,enrollment_year,archived from student_profile where student_id=?", this::map, id);
    }

    public boolean numberTaken(String studentNo, Integer exceptId) throws SQLException {
        String sql = "select student_id from student_profile where student_no=?" + (exceptId == null ? "" : " and student_id<>?");
        Object[] args = exceptId == null ? new Object[]{studentNo} : new Object[]{studentNo, exceptId};
        return SqlRows.one(sql, rs -> rs.getInt(1), args) != null;
    }

    public void save(Student student) throws SQLException {
        SqlRows.change("insert into student_profile(account_id,student_no,full_name,gender,class_name,enrollment_year,archived) values(?,?,?,?,?,?,0)",
                student.getAccountId(), student.getStudentNo(), student.getFullName(), student.getGender(), student.getClassName(), student.getEnrollmentYear());
    }

    public void update(Student student) throws SQLException {
        SqlRows.change("update student_profile set account_id=?, student_no=?, full_name=?, gender=?, class_name=?, enrollment_year=? where student_id=?",
                student.getAccountId(), student.getStudentNo(), student.getFullName(), student.getGender(), student.getClassName(), student.getEnrollmentYear(), student.getStudentId());
    }

    public void archive(int id) throws SQLException {
        SqlRows.change("update student_profile set archived=1 where student_id=?", id);
    }

    public void remove(int id) throws SQLException {
        SqlRows.change("delete from student_profile where student_id=?", id);
    }

    public int countActive() throws SQLException {
        Integer count = SqlRows.one("select count(*) from student_profile where archived=0", rs -> rs.getInt(1));
        return count == null ? 0 : count;
    }

    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student();
        int accountId = rs.getInt("account_id");
        s.setAccountId(rs.wasNull() ? null : accountId);
        s.setStudentId(rs.getInt("student_id"));
        s.setStudentNo(rs.getString("student_no"));
        s.setFullName(rs.getString("full_name"));
        s.setGender(rs.getString("gender"));
        s.setClassName(rs.getString("class_name"));
        s.setEnrollmentYear(rs.getInt("enrollment_year"));
        s.setArchived(rs.getInt("archived"));
        return s;
    }
}
