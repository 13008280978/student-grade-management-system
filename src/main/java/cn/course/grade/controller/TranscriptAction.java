package cn.course.grade.controller;

import cn.course.grade.dao.ScoreRepository;
import cn.course.grade.model.Account;
import cn.course.grade.model.Score;
import cn.course.grade.util.GpaTool;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/transcript")
public class TranscriptAction extends HttpServlet {
    private final ScoreRepository scores = new ScoreRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.loginRequired(req, resp)) {
            return;
        }
        Account current = SessionGate.account(req);
        try {
            Integer scope = SessionGate.staff(current) ? null : current.getAccountId();
            List<Score> rows = scores.list(req.getParameter("term"), req.getParameter("keyword"), scope);
            Map<Integer, String> gpa = new LinkedHashMap<>();
            Map<Integer, String> level = new LinkedHashMap<>();
            for (Score row : rows) {
                gpa.put(row.getScoreId(), GpaTool.point(row.getScoreValue()).toPlainString());
                level.put(row.getScoreId(), GpaTool.level(row.getScoreValue()));
            }
            req.setAttribute("scores", rows);
            req.setAttribute("gpa", gpa);
            req.setAttribute("level", level);
            req.getRequestDispatcher("/WEB-INF/views/transcript.jsp").forward(req, resp);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
