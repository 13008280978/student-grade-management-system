package cn.course.grade.controller;

import cn.course.grade.dao.ScoreRepository;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/analysis")
public class AnalysisAction extends HttpServlet {
    private final ScoreRepository scores = new ScoreRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.managerRequired(req, resp)) {
            return;
        }
        try {
            String term = req.getParameter("term");
            req.setAttribute("bands", scores.bands(term));
            req.setAttribute("ranks", scores.ranking(term));
            req.getRequestDispatcher("/WEB-INF/views/analysis.jsp").forward(req, resp);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
