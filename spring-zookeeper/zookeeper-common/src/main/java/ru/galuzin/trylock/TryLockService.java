package ru.galuzin.trylock;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.galuzin.article_2.DistributedLock;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class TryLockService implements Watcher {

    //    String path;
//
//    ExecutorService executorService = Executors.newCachedThreadPool();
    final static Logger log = LoggerFactory.getLogger(TryLockService.class);
    public static final String TRANSIT_LOCK = "transitLock";
    public static final String BASE_PATH = "/testLock";

    final ZooKeeper zk;

    static final String basePath = "/testLock";

    public TryLockService() throws IOException {
        String zookeeperConnect = "localhost:2181";//System.getProperty("zookeeper.connect");
        zk = new ZooKeeper(zookeeperConnect, 3000, this);
    }

    public void method1(String owner) {
        //check exist
        try {
            if (zk.exists(BASE_PATH, false) == null) {
                zk.create(BASE_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(BASE_PATH + "/" + owner, false) == null) {
                zk.create(BASE_PATH + "/" + owner, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DistributedLock distrLock = new DistributedLock(zk, BASE_PATH +"/"+owner, TRANSIT_LOCK);
        try {
            if (distrLock.tryLock()) {
                //todo await
                //check version or data not null
                await().atMost(60, TimeUnit.SECONDS)
                    .pollInSameThread()
                    .pollInterval(1_000, TimeUnit.MILLISECONDS)
                    .until(() -> {
                        return distrLock.isCanceled();
                    });

                log.info("after await");
            }
        } finally {
            distrLock.unlock();
        }
    }

    public void cancel(String owner) {
        DistributedLock lock = new DistributedLock(zk, BASE_PATH + "/" + owner, TRANSIT_LOCK);
        lock.cancel();
    }

    /**
     * Метод отслеживающий ключевые состояния клиента ZooKeeper
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
//            phaser.arrive();
        } else if (event.getState() == Watcher.Event.KeeperState.Expired) {
//            handler.handleExpired();
        } else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
//            handler.handleDisconnected();
        }
        log.info("*** event {}", event.getType().name());
    }
}
