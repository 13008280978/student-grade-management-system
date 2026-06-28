package cn.course.grade.controller;

import cn.course.grade.dao.ScoreRepository;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/scholarship")
public class ScholarshipAction extends HttpServlet {
    private final ScoreRepository scores = new ScoreRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.managerRequired(req, resp)) {
            return;
        }
        try {
            String minText = req.getParameter("minAverage");
            BigDecimal minAverage = minText == null || minText.trim().isEmpty() ? BigDecimal.valueOf(85) : new BigDecimal(minText);
            req.setAttribute("candidates", scores.scholarship(req.getParameter("term"), minAverage));
            req.getRequestDispatcher("/WEB-INF/views/scholarship.jsp").forward(req, resp);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
