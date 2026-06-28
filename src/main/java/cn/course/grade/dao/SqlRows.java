package cn.course.grade.dao;

import cn.course.grade.util.JdbcKit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

final class SqlRows {
    private SqlRows() {
    }

    interface Mapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    static <T> List<T> list(String sql, Mapper<T> mapper, Object... args) throws SQLException {
        try (Connection conn = JdbcKit.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, args);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> data = new ArrayList<>();
                while (rs.next()) {
                    data.add(mapper.map(rs));
                }
                return data;
            }
        }
    }

    static <T> T one(String sql, Mapper<T> mapper, Object... args) throws SQLException {
        List<T> data = list(sql, mapper, args);
        return data.isEmpty() ? null : data.get(0);
    }

    static int change(String sql, Object... args) throws SQLException {
        try (Connection conn = JdbcKit.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, args);
            return ps.executeUpdate();
        }
    }

    private static void bind(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
    }
}
