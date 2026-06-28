package cn.course.grade.controller;

import cn.course.grade.dao.AccountRepository;
import cn.course.grade.dao.CourseRepository;
import cn.course.grade.dao.ScoreRepository;
import cn.course.grade.dao.StudentRepository;
import cn.course.grade.model.Account;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/home")
public class HomeAction extends HttpServlet {
    private final AccountRepository accounts = new AccountRepository();
    private final StudentRepository students = new StudentRepository();
    private final CourseRepository courses = new CourseRepository();
    private final ScoreRepository scores = new ScoreRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.loginRequired(req, resp)) {
            return;
        }
        Account current = SessionGate.account(req);
        Integer mine = SessionGate.staff(current) ? null : current.getAccountId();
        try {
            req.setAttribute("accountCount", SessionGate.staff(current) ? accounts.countActive() : 1);
            req.setAttribute("studentCount", SessionGate.staff(current) ? students.countActive() : students.search("", mine).size());
            req.setAttribute("courseCount", courses.count());
            req.setAttribute("scoreCount", SessionGate.staff(current) ? scores.count() : scores.list(null, null, mine).size());
            req.setAttribute("avgScore", scores.average(mine));
            req.setAttribute("courseAverages", scores.courseAverages(mine));
            req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
