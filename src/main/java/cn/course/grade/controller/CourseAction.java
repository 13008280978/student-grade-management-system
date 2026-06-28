package cn.course.grade.controller;

import cn.course.grade.dao.CourseRepository;
import cn.course.grade.model.Course;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/courses/*")
public class CourseAction extends HttpServlet {
    private final CourseRepository courses = new CourseRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.loginRequired(req, resp)) {
            return;
        }
        try {
            if ("/edit".equals(req.getPathInfo()) || "/new".equals(req.getPathInfo())) {
                if (!SessionGate.managerRequired(req, resp)) {
                    return;
                }
                if ("/edit".equals(req.getPathInfo())) {
                    req.setAttribute("target", courses.byId(Integer.parseInt(req.getParameter("id"))));
                }
                req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
            } else {
                req.setAttribute("courses", courses.list(req.getParameter("keyword")));
                req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req, resp);
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
                courses.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/courses");
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
        if (courses.codeTaken(req.getParameter("courseCode"), id)) {
            req.setAttribute("error", "课程编号已存在");
            if (id != null) {
                req.setAttribute("target", courses.byId(id));
            }
            req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
            return;
        }
        Course course = new Course();
        course.setCourseId(id);
        course.setCourseCode(req.getParameter("courseCode"));
        course.setCourseName(req.getParameter("courseName"));
        course.setCredit(new BigDecimal(req.getParameter("credit")));
        course.setTeacherName(req.getParameter("teacherName"));
        if (id == null) {
            courses.save(course);
        } else {
            courses.update(course);
        }
        resp.sendRedirect(req.getContextPath() + "/courses");
    }
}
