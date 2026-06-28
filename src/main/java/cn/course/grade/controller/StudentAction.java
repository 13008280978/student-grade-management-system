package cn.course.grade.controller;

import cn.course.grade.dao.StudentRepository;
import cn.course.grade.model.Account;
import cn.course.grade.model.Student;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/students/*")
public class StudentAction extends HttpServlet {
    private final StudentRepository students = new StudentRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.loginRequired(req, resp)) {
            return;
        }
        Account current = SessionGate.account(req);
        try {
            if ("/edit".equals(req.getPathInfo())) {
                if (!SessionGate.managerRequired(req, resp)) {
                    return;
                }
                req.setAttribute("target", students.byId(Integer.parseInt(req.getParameter("id"))));
                req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
            } else if ("/new".equals(req.getPathInfo())) {
                if (!SessionGate.managerRequired(req, resp)) {
                    return;
                }
                req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
            } else {
                Integer scope = SessionGate.staff(current) ? null : current.getAccountId();
                req.setAttribute("students", students.search(req.getParameter("q"), scope));
                req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
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
            String extra = req.getPathInfo();
            if ("/save".equals(extra)) {
                save(req, resp);
            } else if ("/archive".equals(extra)) {
                students.archive(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/students");
            } else if ("/delete".equals(extra)) {
                students.remove(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/students");
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String idText = req.getParameter("id");
        Integer id = idText == null || idText.trim().isEmpty() ? null : Integer.parseInt(idText);
        if (students.numberTaken(req.getParameter("studentNo"), id)) {
            req.setAttribute("error", "学号已存在");
            if (id != null) {
                req.setAttribute("target", students.byId(id));
            }
            req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
            return;
        }
        Student student = new Student();
        student.setStudentId(id);
        String accountText = req.getParameter("accountId");
        student.setAccountId(accountText == null || accountText.trim().isEmpty() ? null : Integer.parseInt(accountText));
        student.setStudentNo(req.getParameter("studentNo"));
        student.setFullName(req.getParameter("fullName"));
        student.setGender(req.getParameter("gender"));
        student.setClassName(req.getParameter("className"));
        student.setEnrollmentYear(Integer.parseInt(req.getParameter("enrollmentYear")));
        if (id == null) {
            students.save(student);
        } else {
            students.update(student);
        }
        resp.sendRedirect(req.getContextPath() + "/students");
    }
}
