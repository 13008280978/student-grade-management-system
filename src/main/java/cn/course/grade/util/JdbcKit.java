package cn.course.grade.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class JdbcKit {
    private static final HikariDataSource POOL;

    static {
        try (InputStream input = JdbcKit.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            if (input == null) {
                throw new IllegalStateException("jdbc.properties 未找到");
            }
            Properties props = new Properties();
            props.load(input);
            HikariConfig cfg = new HikariConfig();
            cfg.setDriverClassName(props.getProperty("jdbc.driverClassName"));
            cfg.setJdbcUrl(props.getProperty("jdbc.url"));
            cfg.setUsername(props.getProperty("jdbc.username"));
            cfg.setPassword(props.getProperty("jdbc.password"));
            cfg.setMaximumPoolSize(Integer.parseInt(props.getProperty("jdbc.poolSize", "10")));
            cfg.setPoolName("grade-center-pool");
            POOL = new HikariDataSource(cfg);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private JdbcKit() {
    }

    public static Connection open() throws SQLException {
        return POOL.getConnection();
    }
}
