package net.ttddyy.dsproxy.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcHelper {
    private static final Logger logger = LoggerFactory.getLogger(JdbcHelper.class);

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("Проблема закрытия ResultSet", e);
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("Проблема закрытия Statement", e);
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Проблема закрытия Connection", e);
        }
    }

    public static void close(ResultSet rs, Statement statement, Connection connection) {
        close(rs);
        close(statement);
        close(connection);
    }

}
