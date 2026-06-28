package cn.course.grade.controller;

import cn.course.grade.dao.AccountRepository;
import cn.course.grade.model.Account;
import cn.course.grade.util.DigestTool;
import cn.course.grade.util.SessionGate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/accounts/*")
public class AccountAction extends HttpServlet {
    private final AccountRepository accounts = new AccountRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionGate.managerRequired(req, resp)) {
            return;
        }
        try {
            String extra = req.getPathInfo();
            if ("/edit".equals(extra)) {
                req.setAttribute("target", accounts.byId(Integer.parseInt(req.getParameter("id"))));
                req.getRequestDispatcher("/WEB-INF/views/account-form.jsp").forward(req, resp);
            } else if ("/password".equals(extra)) {
                req.setAttribute("id", req.getParameter("id"));
                req.getRequestDispatcher("/WEB-INF/views/account-password.jsp").forward(req, resp);
            } else {
                req.setAttribute("accounts", accounts.find(req.getParameter("loginName"), req.getParameter("mailAddress"),
                        req.getParameter("joinedFrom"), req.getParameter("joinedTo")));
                req.getRequestDispatcher("/WEB-INF/views/account-list.jsp").forward(req, resp);
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
            if ("/edit".equals(extra)) {
                update(req, resp);
            } else if ("/password".equals(extra)) {
                resetPassword(req, resp);
            } else if ("/disable".equals(extra)) {
                accounts.disable(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/accounts");
            } else if ("/erase".equals(extra)) {
                accounts.erase(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/accounts");
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        if (accounts.mailTaken(req.getParameter("mailAddress"), id)) {
            req.setAttribute("error", "邮箱已被其他账号使用");
            req.setAttribute("target", accounts.byId(id));
            req.getRequestDispatcher("/WEB-INF/views/account-form.jsp").forward(req, resp);
            return;
        }
        Account account = new Account();
        account.setAccountId(id);
        account.setMailAddress(req.getParameter("mailAddress"));
        account.setMobile(req.getParameter("mobile"));
        account.setRoleCode(req.getParameter("roleCode"));
        accounts.update(account);
        resp.sendRedirect(req.getContextPath() + "/accounts");
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        if (!req.getParameter("newPassword").equals(req.getParameter("repeatPassword"))) {
            req.setAttribute("error", "两次密码不一致");
            req.setAttribute("id", req.getParameter("id"));
            req.getRequestDispatcher("/WEB-INF/views/account-password.jsp").forward(req, resp);
            return;
        }
        Account manager = SessionGate.account(req);
        if (accounts.verify(manager.getLoginName(), DigestTool.sha256Hex(req.getParameter("managerPassword"))) == null) {
            req.setAttribute("error", "管理员密码验证失败");
            req.setAttribute("id", req.getParameter("id"));
            req.getRequestDispatcher("/WEB-INF/views/account-password.jsp").forward(req, resp);
            return;
        }
        accounts.changePassword(Integer.parseInt(req.getParameter("id")), DigestTool.sha256Hex(req.getParameter("newPassword")));
        resp.sendRedirect(req.getContextPath() + "/accounts");
    }
}
