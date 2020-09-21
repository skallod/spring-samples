package ru.galuzin.article_2;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DistributedLock {
    private final ZooKeeper zk;
    private final String lockBasePath;
    private final String lockName;
    private String lockPath;
    public DistributedLock(ZooKeeper zk, String lockBasePath, String lockName) {
        this.zk = zk;
        this.lockBasePath = lockBasePath;
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

    public boolean trylock() throws Exception {
        try {
// lockPath will be different than (lockBasePath + "/" + lockName) becuase of the sequence number ZooKeeper appends
            //lockPath = zk.create(lockBasePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            lockPath = zk.create(lockBasePath + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> nodes = zk.getChildren(lockBasePath, false);
            Collections.sort(nodes); // ZooKeeper node names can be sorted lexographically
            if (lockPath.endsWith(nodes.get(0))) {
                return true;
            }
            return false;
        } catch (KeeperException e) {
            throw new IOException (e);
        } catch (InterruptedException e) {
            throw new IOException (e);
        }
    }

    public void unlock() throws IOException {
        try {
            zk.delete(lockPath, -1);
            lockPath = null;
        } catch (KeeperException e) {
            throw new IOException (e);
        } catch (InterruptedException e) {
            throw new IOException (e);
        }
    }
}
