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

@WebServlet("/program")
public class ProgramAction extends HttpServlet {
    private final ScoreRepository scores = new ScoreRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.managerRequired(req, resp)) {
            return;
        }
        try {
            String required = req.getParameter("requiredCredit");
            BigDecimal requiredCredit = required == null || required.trim().isEmpty() ? BigDecimal.valueOf(12) : new BigDecimal(required);
            req.setAttribute("progressRows", scores.programProgress(requiredCredit));
            req.getRequestDispatcher("/WEB-INF/views/program.jsp").forward(req, resp);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
