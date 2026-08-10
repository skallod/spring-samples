package ru.pesok.graviy.spring.orm.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@ConditionalOnProperty(value = "transaction.logger.enabled", havingValue = "true")
public class TransactionLogService {

    private final ThreadLocal<Deque<String>> txIdStack = ThreadLocal.withInitial(ArrayDeque::new);

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalMethod() {}

    @Pointcut("@within(org.springframework.transaction.annotation.Transactional)")
    public void transactionalClass() {}

    @Pointcut("transactionalMethod() || transactionalClass()")
    public void anyTransactional() {}

    @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.*(..)) || " +
        "execution(* ru.pesok.graviy.spring.orm.repository..*Repository.*(..))")
    public void repositoryMethod() {}

    @Around("anyTransactional()")
    public Object logTransactionalMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String txId = enterTx();
        String threadId = Thread.currentThread().getName();
        String methodName = joinPoint.getSignature().toShortString();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        MDC.put("threadId", threadId);

        Transactional txAnnotation = getTransactionalAnnotation(joinPoint);
        String txMeta = formatTxMeta(txAnnotation);

        long start = System.nanoTime();

        log.info("[TRX:{}][THRID:{}][CLS:{}] >>> TRX-METHOD: {} {} args={}",
            txId, threadId, className, methodName, txMeta,
            formatArgs(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();

            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            String txName = TransactionSynchronizationManager.getCurrentTransactionName();

            if (!active) {
                log.info("[TRX:{}][THRID:{}][CLS:{}] <<< TRX-METHOD: {} OK ({}ms) resultType={} [NO ACTIVE TX!]",
                    txId, threadId, className, methodName, durationMs,
                    result != null ? result.getClass().getSimpleName() : "void");
            } else {
                log.info("[TRX:{}][THRID:{}][CLS:{}] <<< TRX-METHOD: {} OK ({}ms) active={} readOnly={} txName={} resultType={}",
                    txId, threadId, className, methodName, durationMs, active, readOnly, txName,
                    result != null ? result.getClass().getSimpleName() : "void");
            }

            return result;

        } catch (Exception e) {
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            boolean active = TransactionSynchronizationManager.isActualTransactionActive();

            log.error("[TRX:{}][THRID:{}][CLS:{}] <<< TRX-METHOD: {} FAILED ({}ms) {} active={} rollbackOnly={} error={}",
                txId, threadId, className, methodName, durationMs, txMeta, active,
                e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw e;

        } finally {
            exitTx();
        }
    }

    @Around("repositoryMethod()")
    public Object logRepositoryMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String txId = enterTx();
        String threadId = Thread.currentThread().getName();
        String methodName = joinPoint.getSignature().toShortString();
        String operation = detectOperation(methodName);
        Object[] args = joinPoint.getArgs();

        MDC.put("threadId", threadId);

        Long entityId = extractEntityId(args);
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();

        long start = System.nanoTime();

        if (entityId != null) {
            log.info("[TRX:{}][THRID:{}][REPO] >>> {} id={} method={} active={} args={}",
                txId, threadId, operation, entityId, methodName, active, formatArgs(args));
        } else {
            log.info("[TRX:{}][THRID:{}][REPO] >>> {} method={} active={} args={}",
                txId, threadId, operation, methodName, active, formatArgs(args));
        }

        try {
            Object result = joinPoint.proceed();
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            if (result instanceof Iterable) {
                long count = countIterable((Iterable<?>) result);
                log.info("[TRX:{}][THRID:{}][REPO] <<< {} DONE ({}ms) active={} returned {} items",
                    txId, threadId, operation, durationMs, active, count);
            } else {
                log.info("[TRX:{}][THRID:{}][REPO] <<< {} DONE ({}ms) active={} resultType={}",
                    txId, threadId, operation, durationMs, active,
                    result != null ? result.getClass().getSimpleName() : "null");
            }

            return result;

        } catch (Exception e) {
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            log.error("[TRX:{}][THRID:{}][REPO] <<< {} FAILED ({}ms) active={} error={}",
                txId, threadId, operation, durationMs, active,
                e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw e;

        } finally {
            exitTx();
        }
    }

    private String enterTx() {
        Deque<String> stack = txIdStack.get();
        String txId = MDC.get("txId");

        if (txId == null || stack.isEmpty()) {
            txId = UUID.randomUUID().toString().substring(0, 8);
            MDC.put("txId", txId);
        }

        stack.push(txId);
        return txId;
    }

    private void exitTx() {
        Deque<String> stack = txIdStack.get();
        if (!stack.isEmpty()) {
            stack.pop();
        }

        if (stack.isEmpty()) {
            MDC.remove("txId");
            MDC.remove("threadId");
            txIdStack.remove();
        }
    }

    private Transactional getTransactionalAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return AnnotationUtils.findAnnotation(method, Transactional.class);
    }

    private String formatTxMeta(Transactional tx) {
        if (tx == null) return "[no-trx-annotation]";
        return String.format("isolation=%s propagation=%s readOnly=%s timeout=%d",
            tx.isolation(), tx.propagation(), tx.readOnly(), tx.timeout());
    }

    private String detectOperation(String methodName) {
        String lower = methodName.toLowerCase();
        if (lower.contains("save") || lower.contains("persist") || lower.contains("merge")) {
            return "SAVE";
        }
        if (lower.contains("update") || lower.contains("modify")) {
            return "UPDATE";
        }
        if (lower.contains("delete") || lower.contains("remove") || lower.contains("erase")) {
            return "DELETE";
        }
        if (lower.contains("find") || lower.contains("get") || lower.contains("select") ||
            lower.contains("read") || lower.contains("query")) {
            return "SELECT";
        }
        return "REPO-CALL";
    }

    private Long extractEntityId(Object[] args) {
        if (args == null) return null;
        for (Object arg : args) {
            if (arg instanceof Long) return (Long) arg;
            if (arg instanceof Number) return ((Number) arg).longValue();
            if (arg instanceof String) {
                try {
                    return Long.parseLong((String) arg);
                } catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        try {
            return Arrays.toString(args);
        } catch (Exception e) {
            return "[unable-to-format]";
        }
    }

    private long countIterable(Iterable<?> iterable) {
        if (iterable == null) return 0;
        long count = 0;
        for (Object ignored : iterable) {
            count++;
            if (count > 1000) break;
        }
        return count;
    }
}
