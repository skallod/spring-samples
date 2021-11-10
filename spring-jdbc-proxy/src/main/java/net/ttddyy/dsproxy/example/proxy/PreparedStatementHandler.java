package net.ttddyy.dsproxy.example.proxy;

import net.ttddyy.dsproxy.ConnectionInfo;
import net.ttddyy.dsproxy.StatementType;
import net.ttddyy.dsproxy.proxy.ProxyConfig;
import net.ttddyy.dsproxy.proxy.StatementProxyLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PreparedStatementHandler extends ReturnInvocationHandler {
    public PreparedStatementHandler(PreparedStatement ps,
                                    String query,
                                    ConnectionInfo connectionInfo,
                                    Connection proxyConnection,
                                    ProxyConfig proxyConfig,
                                    boolean generateKey) {
        super(
                StatementProxyLogic.Builder.create()
                        .statement(ps, StatementType.PREPARED)
                        .query(query)
                        .connectionInfo(connectionInfo)
                        .proxyConnection(proxyConnection)
                        .proxyConfig(proxyConfig)
                        .generateKey(generateKey)
                        .build()
        );
    }

    @Override
    public boolean execute(Object[] args) throws Throwable {
        return (Boolean) this.delegate.invoke(PreparedStatement.class.getMethod(EXECUTE), new Object[0]);
    }

    @Override
    public long count(Object[] args) throws Throwable {
        return (int) this.delegate.invoke(PreparedStatement.class.getMethod(GET_UPDATE_COUNT), new Object[0]);
    }

    @Override
    public long largeCount(Object[] args) throws Throwable {
        return (long) this.delegate.invoke(PreparedStatement.class.getMethod(GET_LARGE_UPDATE_COUNT), new Object[0]);
    }


}
