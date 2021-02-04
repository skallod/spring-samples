package ru.galuzin.trylock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TryLockMain {

    private static final Logger log = LoggerFactory.getLogger(TryLockMain.class);

    final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        TryLockMain tryLockMain = new TryLockMain();
        TryLockService tryLockService = new TryLockService();
        String owner = "towner";
        CompletableFuture.runAsync(()-> tryLockService.method1(owner), tryLockMain.executorService);
        CompletableFuture.runAsync(()-> {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                log.error("cancel interrupt",e);
            }
            log.info("before cancel");
            //tryLockService.cancel(owner);
        }, tryLockMain.executorService);
    }
}
