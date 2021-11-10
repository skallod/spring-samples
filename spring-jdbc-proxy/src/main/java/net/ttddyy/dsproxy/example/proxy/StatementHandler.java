package net.ttddyy.dsproxy.example.proxy;

import net.ttddyy.dsproxy.ConnectionInfo;
import net.ttddyy.dsproxy.StatementType;
import net.ttddyy.dsproxy.proxy.ProxyConfig;
import net.ttddyy.dsproxy.proxy.StatementProxyLogic;

import java.sql.Connection;
import java.sql.Statement;

public class StatementHandler extends ReturnInvocationHandler {

    public StatementHandler(Statement stmt, ConnectionInfo connectionInfo, Connection proxyConnection, ProxyConfig proxyConfig) {
        super(
                StatementProxyLogic.Builder.create()
                        .statement(stmt, StatementType.STATEMENT)
                        .connectionInfo(connectionInfo)
                        .proxyConnection(proxyConnection)
                        .proxyConfig(proxyConfig)
                        .build()
        );
    }

    @Override
    public boolean execute(Object[] args) throws Throwable {
        return (Boolean) this.delegate.invoke(Statement.class.getMethod(EXECUTE, String.class), new Object[]{args[0]});
    }

    @Override
    public long count(Object[] args) throws Throwable {
        return (int) this.delegate.invoke(Statement.class.getMethod(GET_UPDATE_COUNT), new Object[0]);
    }

    @Override
    public long largeCount(Object[] args) throws Throwable {
        return (long) this.delegate.invoke(Statement.class.getMethod(GET_LARGE_UPDATE_COUNT), new Object[0]);
    }

}
