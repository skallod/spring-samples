package net.ttddyy.dsproxy.example;


import oracle.jdbc.OraclePreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;

public class OracleStatementUnwrapper {

    public static OraclePreparedStatement unwrap(Statement statement) throws SQLException {
        if (statement instanceof OraclePreparedStatement) {
            return (OraclePreparedStatement) statement;
        } else {
            //Например, если используется hikari jdbc pool
            return statement.unwrap(OraclePreparedStatement.class);
        }
    }
}
