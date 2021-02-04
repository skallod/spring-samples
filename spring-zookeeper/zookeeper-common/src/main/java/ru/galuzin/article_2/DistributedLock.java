package ru.galuzin.article_2;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DistributedLock {

    static final Logger log = LoggerFactory.getLogger(DistributedLock.class);

    private final ZooKeeper zk;
    private final String lockBasePath;
    private final String lockName;
    private String lockPath;

    public DistributedLock(ZooKeeper zk, String lockBasePath, String lockName) {
        this.zk = zk;
        this.lockBasePath = lockBasePath + "/" + lockName;
        this.lockName = lockName;
    }

    public void lock() throws Exception {
        try {
// lockPath will be different than (lockBasePath + "/" + lockName) becuase of the sequence number ZooKeeper appends
            //lockPath = zk.create(lockBasePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            lockPath = zk.create(lockBasePath + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            final Object lock = new Object();
            synchronized(lock) {
                while(true) {
                    List<String> nodes = zk.getChildren(lockBasePath, new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            synchronized (lock) {
                                lock.notifyAll();
                            }
                        }
                    });
                    Collections.sort(nodes); // ZooKeeper node names can be sorted lexographically
                    if (lockPath.endsWith(nodes.get(0))) {
                        return;
                    } else {
                        lock.wait();
                    }
                }
            }
        } catch (KeeperException e) {
            throw new IOException (e);
        } catch (InterruptedException e) {
            throw new IOException (e);
        }
    }

//    public static class TryLockResult {
//        public boolean lockGot;
//        public String lockPath;
//
//        public TryLockResult(boolean lockGot, String lockPath) {
//            this.lockGot = lockGot;
//            this.lockPath = lockPath;
//        }
//    }

    public boolean tryLock() {
        try {
            createIfNotExist();
// lockPath will be different than (lockBasePath + "/" + lockName) becuase of the sequence number ZooKeeper appends
            //lockPath = zk.create(lockBasePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            lockPath = zk.create(lockBasePath + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> nodes = zk.getChildren(lockBasePath, false);
            Collections.sort(nodes); // ZooKeeper node names can be sorted lexographically
            boolean b = lockPath.endsWith(nodes.get(0));
            if (b) {
                log.info("Lock acquired {}", lockPath);
            }
            return b;
        } catch (Exception e) {
//            throw new IOException (e);
            log.error("Distr lock try lock fail", e);
            return false;
        }
    }

    void createIfNotExist() throws Exception {
        //todo sync
        if (zk.exists(lockBasePath,false)==null) {
            zk.create(lockBasePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    public void unlock() {
        try {
            if (lockPath!=null) {
                zk.delete(lockPath, -1);
                log.info("Lock was deleted {}", lockPath);
                lockPath = null;
            }
        } catch (Exception e) {
            log.error("Distr unlock fail", e);
            throw new RuntimeException(e);
        }
    }

    public void cancel() {
        try {
            //todo sync
            List<String> nodes = zk.getChildren(lockBasePath, false);
            Collections.sort(nodes);
            if (!nodes.isEmpty()) {
                log.info("nodes {}", nodes.get(0));
                zk.setData(lockBasePath + "/" + nodes.get(0), new byte[]{1}, -1);
                log.info("cancel set data");
            }
        } catch (Exception e) {
            log.error("Distr cancel fail", e);
            throw new RuntimeException(e);
        }
    }

    public boolean isCanceled(){
        try {
            if (lockPath!=null) {
                //todo sync
                byte[] data = zk.getData(lockPath, false, null);
                if (data!=null) {
                    log.info("is cancel, cancel");
                    return true;
                } else {
                    log.info("is cancel, data is null");
                }
            }
            return false;
        } catch (Exception e) {
            log.error("Distr check fail", e);
            throw new RuntimeException(e);
        }
    }

    public String getLockPath() {
        return lockPath;
    }
}
