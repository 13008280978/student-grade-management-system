package cn.course.grade.dao;

import cn.course.grade.model.Account;
import cn.course.grade.util.JdbcKit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
    private static final String ACCOUNT_SELECT =
            "select a.account_id,a.login_name,a.mail_address,a.mobile,a.disabled,a.joined_on," +
                    "r.role_code,r.role_title from sys_account a join sys_role r on a.role_id=r.role_id ";

    public Account verify(String loginName, String digest) throws SQLException {
        return SqlRows.one(ACCOUNT_SELECT + "where a.login_name=? and a.pass_digest=? and a.disabled=0",
                this::map, loginName, digest);
    }

    public boolean loginTaken(String loginName) throws SQLException {
        return SqlRows.one("select account_id from sys_account where login_name=?",
                rs -> rs.getInt(1), loginName) != null;
    }

    public boolean mailTaken(String mail, Integer exceptId) throws SQLException {
        String sql = "select account_id from sys_account where mail_address=?" + (exceptId == null ? "" : " and account_id<>?");
        Object[] args = exceptId == null ? new Object[]{mail} : new Object[]{mail, exceptId};
        return SqlRows.one(sql, rs -> rs.getInt(1), args) != null;
    }

    public void create(Account account, String digest) throws SQLException {
        SqlRows.change("insert into sys_account(login_name,pass_digest,mail_address,mobile,role_id,disabled,joined_on) " +
                        "values(?,?,?,?,(select role_id from sys_role where role_code=?),0,curdate())",
                account.getLoginName(), digest, account.getMailAddress(), account.getMobile(), account.getRoleCode());
    }

    public List<Account> find(String loginName, String mail, String from, String to) throws SQLException {
        StringBuilder sql = new StringBuilder(ACCOUNT_SELECT + "where 1=1");
        List<Object> args = new ArrayList<>();
        if (loginName != null && !loginName.trim().isEmpty()) {
            sql.append(" and a.login_name like ?");
            args.add("%" + loginName.trim() + "%");
        }
        if (mail != null && !mail.trim().isEmpty()) {
            sql.append(" and a.mail_address like ?");
            args.add("%" + mail.trim() + "%");
        }
        if (from != null && !from.trim().isEmpty()) {
            sql.append(" and a.joined_on>=?");
            args.add(LocalDate.parse(from.trim()));
        }
        if (to != null && !to.trim().isEmpty()) {
            sql.append(" and a.joined_on<=?");
            args.add(LocalDate.parse(to.trim()));
        }
        sql.append(" order by a.account_id desc");
        return SqlRows.list(sql.toString(), this::map, args.toArray());
    }

    public Account byId(int id) throws SQLException {
        return SqlRows.one(ACCOUNT_SELECT + "where a.account_id=?", this::map, id);
    }

    public void update(Account account) throws SQLException {
        SqlRows.change("update sys_account set mail_address=?, mobile=?, role_id=(select role_id from sys_role where role_code=?) where account_id=?",
                account.getMailAddress(), account.getMobile(), account.getRoleCode(), account.getAccountId());
    }

    public void changePassword(int id, String digest) throws SQLException {
        SqlRows.change("update sys_account set pass_digest=? where account_id=?", digest, id);
    }

    public void disable(int id) throws SQLException {
        SqlRows.change("update sys_account set disabled=1 where account_id=? and role_id<>(select role_id from sys_role where role_code='manager')", id);
    }

    public void erase(int id) throws SQLException {
        try (Connection conn = JdbcKit.open()) {
            conn.setAutoCommit(false);
            try (PreparedStatement detachStudent = conn.prepareStatement("update student_profile set account_id=null where account_id=?");
                 PreparedStatement deleteAccount = conn.prepareStatement("delete from sys_account where account_id=? and role_id<>(select role_id from sys_role where role_code='manager')")) {
                detachStudent.setInt(1, id);
                detachStudent.executeUpdate();
                deleteAccount.setInt(1, id);
                deleteAccount.executeUpdate();
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public int countActive() throws SQLException {
        Integer total = SqlRows.one("select count(*) from sys_account where disabled=0", rs -> rs.getInt(1));
        return total == null ? 0 : total;
    }

    private Account map(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setLoginName(rs.getString("login_name"));
        account.setMailAddress(rs.getString("mail_address"));
        account.setMobile(rs.getString("mobile"));
        account.setDisabled(rs.getInt("disabled"));
        account.setRoleCode(rs.getString("role_code"));
        account.setRoleTitle(rs.getString("role_title"));
        if (rs.getDate("joined_on") != null) {
            account.setJoinedOn(rs.getDate("joined_on").toLocalDate());
        }
        return account;
    }
}
