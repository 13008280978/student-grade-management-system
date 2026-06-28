package cn.course.grade.controller;

import cn.course.grade.dao.ScoreRepository;
import cn.course.grade.model.Account;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/credits")
public class CreditAction extends HttpServlet {
    private final ScoreRepository scores = new ScoreRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.loginRequired(req, resp)) {
            return;
        }
        Account current = SessionGate.account(req);
        Integer scope = SessionGate.staff(current) ? null : current.getAccountId();
        try {
            req.setAttribute("audits", scores.creditAudit(req.getParameter("term"), scope));
            req.getRequestDispatcher("/WEB-INF/views/credits.jsp").forward(req, resp);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
