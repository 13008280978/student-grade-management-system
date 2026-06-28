package cn.course.grade.util;

import cn.course.grade.model.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class SessionGate {
    private SessionGate() {
    }

    public static Account account(HttpServletRequest request) {
        Object value = request.getSession().getAttribute("signedAccount");
        return value instanceof Account ? (Account) value : null;
    }

    public static boolean staff(Account account) {
        return account != null && "manager".equals(account.getRoleCode());
    }

    public static boolean loginRequired(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (account(request) == null) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return false;
        }
        return true;
    }

    public static boolean managerRequired(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Account current = account(request);
        if (current == null) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return false;
        }
        if (!staff(current)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "需要教务管理员权限");
            return false;
        }
        return true;
    }
}
