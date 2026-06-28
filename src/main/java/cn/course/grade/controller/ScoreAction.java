package cn.course.grade.controller;

import cn.course.grade.dao.CourseRepository;
import cn.course.grade.dao.ScoreRepository;
import cn.course.grade.dao.StudentRepository;
import cn.course.grade.model.Account;
import cn.course.grade.model.Score;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/scores/*")
public class ScoreAction extends HttpServlet {
    private final ScoreRepository scores = new ScoreRepository();
    private final StudentRepository students = new StudentRepository();
    private final CourseRepository courses = new CourseRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.loginRequired(req, resp)) {
            return;
        }
        Account current = SessionGate.account(req);
        try {
            if ("/edit".equals(req.getPathInfo()) || "/new".equals(req.getPathInfo())) {
                if (!SessionGate.managerRequired(req, resp)) {
                    return;
                }
                if ("/edit".equals(req.getPathInfo())) {
                    req.setAttribute("target", scores.byId(Integer.parseInt(req.getParameter("id"))));
                }
                req.setAttribute("students", students.allActive());
                req.setAttribute("courses", courses.list(null));
                req.getRequestDispatcher("/WEB-INF/views/score-form.jsp").forward(req, resp);
            } else {
                Integer scope = SessionGate.staff(current) ? null : current.getAccountId();
                req.setAttribute("scores", scores.list(req.getParameter("term"), req.getParameter("keyword"), scope));
                req.getRequestDispatcher("/WEB-INF/views/score-list.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!SessionGate.managerRequired(req, resp)) {
            return;
        }
        try {
            if ("/delete".equals(req.getPathInfo())) {
                scores.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/scores");
                return;
            }
            save(req, resp);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String idText = req.getParameter("id");
        Integer id = idText == null || idText.trim().isEmpty() ? null : Integer.parseInt(idText);
        Integer studentId = Integer.parseInt(req.getParameter("studentId"));
        Integer courseId = Integer.parseInt(req.getParameter("courseId"));
        String term = req.getParameter("termLabel");
        if (scores.duplicated(studentId, courseId, term, id)) {
            req.setAttribute("error", "同一学生、课程和学期的成绩已存在");
            if (id != null) {
                req.setAttribute("target", scores.byId(id));
            }
            req.setAttribute("students", students.allActive());
            req.setAttribute("courses", courses.list(null));
            req.getRequestDispatcher("/WEB-INF/views/score-form.jsp").forward(req, resp);
            return;
        }
        Score score = new Score();
        score.setScoreId(id);
        score.setStudentId(studentId);
        score.setCourseId(courseId);
        score.setTermLabel(term);
        score.setScoreValue(new BigDecimal(req.getParameter("scoreValue")));
        score.setRemark(req.getParameter("remark"));
        if (id == null) {
            scores.save(score);
        } else {
            scores.update(score);
        }
        resp.sendRedirect(req.getContextPath() + "/scores");
    }
}
