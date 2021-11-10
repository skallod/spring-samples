package net.ttddyy.dsproxy.example.proxy;

import net.ttddyy.dsproxy.ConnectionInfo;
import net.ttddyy.dsproxy.proxy.ProxyConfig;
import net.ttddyy.dsproxy.proxy.ProxyJdbcObject;
import net.ttddyy.dsproxy.proxy.jdk.ConnectionInvocationHandler;
import net.ttddyy.dsproxy.proxy.jdk.JdkJdbcProxyFactory;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.function.Function;

public class ReturnProxyFactory extends JdkJdbcProxyFactory {

//    Function<Statement,Statement> statementUnwrapper = StatementUnwrapper::unwrap;


    @Override
    public Connection createConnection(Connection connection, ConnectionInfo connectionInfo, ProxyConfig proxyConfig) {
        return (Connection) Proxy.newProxyInstance(ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ProxyJdbcObject.class, Connection.class},
                new ConnectionInvocationHandler(connection, connectionInfo, proxyConfig));
    }

    public Statement createStatement(Statement statement, ConnectionInfo connectionInfo, Connection proxyConnection, ProxyConfig proxyConfig) {
        return (Statement) Proxy.newProxyInstance(
                ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ProxyJdbcObject.class, Statement.class},
                new StatementHandler(statement, connectionInfo, proxyConnection, proxyConfig)
        );
    }

    public PreparedStatement createPreparedStatement(PreparedStatement preparedStatement, String query, ConnectionInfo connectionInfo, Connection proxyConnection, ProxyConfig proxyConfig, boolean generateKey) {

        return (PreparedStatement) Proxy.newProxyInstance(
                ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ProxyJdbcObject.class, PreparedStatement.class},
                new PreparedStatementHandler(preparedStatement, query, connectionInfo, proxyConnection, proxyConfig, generateKey)
        );
    }

}
