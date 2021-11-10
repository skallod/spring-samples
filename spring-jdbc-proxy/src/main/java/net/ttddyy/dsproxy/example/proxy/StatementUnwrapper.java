package net.ttddyy.dsproxy.example.proxy;

import com.zaxxer.hikari.pool.HikariProxyPreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;

public class StatementUnwrapper {

    public static Statement unwrap(Statement st) {
        if (st instanceof HikariProxyPreparedStatement) {
            try {
                return st.unwrap(Statement.class);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new RuntimeException(throwables);
            }
        }
        return st;
    }
}
