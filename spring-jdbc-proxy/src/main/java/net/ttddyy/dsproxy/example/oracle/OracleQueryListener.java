package net.ttddyy.dsproxy.example.oracle;

import net.ttddyy.dsproxy.example.OracleStatementUnwrapper;
import net.ttddyy.dsproxy.example.service.QueryListener;
import oracle.jdbc.OraclePreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class OracleQueryListener implements QueryListener {

    ThreadLocal<Boolean> isReturningParamsRegistered = ThreadLocal.withInitial(() -> false);

    @Override
    public void beforeQuery(Statement statement) throws SQLException {
        OraclePreparedStatement ops = OracleStatementUnwrapper.unwrap(statement);
        if (isReturningParamsRegistered.get()) {
            // params already registered
            return;
        }
        ops.registerReturnParameter(3, Types.NUMERIC);
        ops.registerReturnParameter(4, Types.VARCHAR);
        ops.registerReturnParameter(5, Types.VARCHAR);
    }

    @Override
    public void afterQuery(Statement statement) throws SQLException {
        OraclePreparedStatement ops = OracleStatementUnwrapper.unwrap(statement);
        if (isReturningParamsRegistered.get()) {
            isReturningParamsRegistered.remove();
            ResultSet rs = ops.getGeneratedKeys();
            int size = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= size; i++) {
                    if ("TIMESTAMP".equals(rs.getMetaData().getColumnTypeName(i))) {
                        System.out.println("gal after query " + rs.getMetaData().getColumnName(i) + " ; " + rs.getDate(i));
                    } else {
                        System.out.println("gal afger query " + rs.getMetaData().getColumnName(i) + " ; " + rs.getObject(i));
                    }
                }
            }
        } else {
            ResultSet rs = ops.getReturnResultSet();
            rs.next();
            System.out.println("gal after query " + rs.getObject(1));//not work rs.getMetaData().getColumnName(1));
            System.out.println("gal after query " + rs.getObject(2));
            System.out.println("gal after query " + rs.getObject(3));
        }
    }

    @Override
    public void returningRegistered(boolean registered) {
        isReturningParamsRegistered.set(registered);
    }
}
