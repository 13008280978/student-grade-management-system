package cn.course.grade.controller;

import cn.course.grade.dao.AccountRepository;
import cn.course.grade.model.Account;
import cn.course.grade.util.DigestTool;
import cn.course.grade.util.ThrottleBox;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet({"/signin", "/signup", "/signout", "/language"})
public class AuthAction extends HttpServlet {
    private final AccountRepository accounts = new AccountRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/language".equals(path)) {
            req.getSession().setAttribute("pageLocale", "en".equalsIgnoreCase(req.getParameter("to")) ? Locale.US : Locale.CHINA);
            String back = req.getHeader("Referer");
            resp.sendRedirect(back == null ? req.getContextPath() + "/home" : back);
            return;
        }
        if ("/signout".equals(path)) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/signin");
            return;
        }
        refreshCheckCode(req);
        req.getRequestDispatcher("/signup".equals(path) ? "/WEB-INF/views/signup.jsp" : "/WEB-INF/views/signin.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if ("/signup".equals(req.getServletPath())) {
                signup(req, resp);
            } else {
                signin(req, resp);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void signin(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String ip = clientIp(req);
        if (ThrottleBox.locked(ip)) {
            show(req, resp, "/WEB-INF/views/signin.jsp", "登录失败次数过多，请稍后再试");
            return;
        }
        if (!checkCode(req)) {
            ThrottleBox.bad(ip);
            show(req, resp, "/WEB-INF/views/signin.jsp", "验证码不正确");
            return;
        }
        Account account = accounts.verify(req.getParameter("loginName"), DigestTool.sha256Hex(req.getParameter("password")));
        if (account == null) {
            ThrottleBox.bad(ip);
            show(req, resp, "/WEB-INF/views/signin.jsp", "账号或密码错误");
            return;
        }
        ThrottleBox.pass(ip);
        req.getSession().setAttribute("signedAccount", account);
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    private void signup(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        if (!checkCode(req)) {
            show(req, resp, "/WEB-INF/views/signup.jsp", "验证码不正确");
            return;
        }
        if (accounts.loginTaken(req.getParameter("loginName"))) {
            show(req, resp, "/WEB-INF/views/signup.jsp", "登录名已存在");
            return;
        }
        if (accounts.mailTaken(req.getParameter("mailAddress"), null)) {
            show(req, resp, "/WEB-INF/views/signup.jsp", "邮箱已被使用");
            return;
        }
        Account account = new Account();
        account.setLoginName(req.getParameter("loginName"));
        account.setMailAddress(req.getParameter("mailAddress"));
        account.setMobile(req.getParameter("mobile"));
        account.setRoleCode("learner");
        accounts.create(account, DigestTool.sha256Hex(req.getParameter("password")));
        resp.sendRedirect(req.getContextPath() + "/signin");
    }

    private void refreshCheckCode(HttpServletRequest req) {
        req.getSession().setAttribute("checkCode", String.valueOf(ThreadLocalRandom.current().nextInt(10000, 99999)));
    }

    private boolean checkCode(HttpServletRequest req) {
        Object code = req.getSession().getAttribute("checkCode");
        return code != null && code.toString().equalsIgnoreCase(req.getParameter("checkCode"));
    }

    private void show(HttpServletRequest req, HttpServletResponse resp, String jsp, String error) throws ServletException, IOException {
        refreshCheckCode(req);
        req.setAttribute("error", error);
        req.getRequestDispatcher(jsp).forward(req, resp);
    }

    private String clientIp(HttpServletRequest req) {
        String value = req.getHeader("X-Real-IP");
        return value == null || value.trim().isEmpty() ? req.getRemoteAddr() : value.trim();
    }
}
