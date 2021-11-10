package net.ttddyy.dsproxy.example.proxy;

import net.ttddyy.dsproxy.proxy.StatementProxyLogic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

public abstract class ReturnInvocationHandler implements InvocationHandler {
    public static final String EXECUTE_UPDATE = "executeUpdate";
    public static final String EXCEUTE_LARGE_UPDATE = "executeLargeUpdate";
    public static final String EXECUTE = "execute";
    public static final String GET_UPDATE_COUNT = "getUpdateCount";
    public static final String GET_LARGE_UPDATE_COUNT = "getLargeUpdateCount";

    private static final String LONG = "long";
    private static final String INT = "int";

    protected final StatementProxyLogic delegate;
    protected final Set<String> methods;

    public ReturnInvocationHandler(StatementProxyLogic delegate) {
        this(delegate,Set.of(EXECUTE_UPDATE, EXCEUTE_LARGE_UPDATE));
    }

    public ReturnInvocationHandler(StatementProxyLogic delegate, Set<String> methods) {
        this.delegate = delegate;
        this.methods = methods;
    }

    public abstract boolean execute(Object[] args) throws Throwable;

    public abstract long count(Object[] args) throws Throwable;

    public abstract long largeCount(Object[] args) throws Throwable;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        /*
            redirecting
            executeUpdate       -> execute
            executeLargeUpdate  -> execute
         */
        if (methods.contains(methodName)) {
            long updateCount = 0L;
            boolean isResultSet = execute(args);
            if (!isResultSet) {
                updateCount = EXECUTE_UPDATE.equals(methodName) ? count(args) : largeCount(args);
            }
            return cast(method.getReturnType(), isResultSet ? 1L : updateCount);
        }
        return this.delegate.invoke(method, args);
    }

    public static Object cast(Class<?> clazz, long val) {
        if (INT.equals(clazz.getName())) {
            return (int) val;
        }
        return val;
    }

}

