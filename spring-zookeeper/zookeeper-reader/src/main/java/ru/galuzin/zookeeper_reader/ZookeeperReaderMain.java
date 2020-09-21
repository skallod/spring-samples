package ru.galuzin.zookeeper_reader;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.KeeperException;
import ru.galuzin.article_2.DistributedLock;
import ru.galuzin.zookeeper.common.ZookeeperHelper;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ZookeeperReaderMain {

    public static void main(String[] args) throws Exception {
        int sleepMsBetweenRetries = 100;
        int maxRetries = 3;
        RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2182", retryPolicy);
        client.start();

//        String str = "/graviy/test/value1";
//        ZookeeperHelper.writeValue(client,str);
//        ZookeeperHelper.sync(client, str);
//        ZookeeperHelper.readValue(client, str);

//        String lockPath = "/mutexA";
//        String lockName = "lockName";
//        DistributedLock lock = new DistributedLock(client.getZookeeperClient().getZooKeeper(), lockPath, lockName);
//        lock.lock();
//        System.out.println("lock was got ");

        System.out.println("phase 1");
        String key = "/client/A/state";
        ZookeeperHelper.initPath(client, key);
        ZookeeperHelper.DataWithVersion dataWithVersion = ZookeeperHelper.readValue(client, key);
        System.out.println("cur version " + dataWithVersion.version);
        Thread.sleep(60_000);
        System.out.println("phase 2");
        try {
            ZookeeperHelper.writeValue(client, key, dataWithVersion.value + 1, dataWithVersion.version);
        }catch (KeeperException.BadVersionException bve) {
            System.out.println("bve.getMessage() = " + bve.getMessage());
        }
        System.out.println("end");

        Thread.sleep(60_000);
        client.close();
    }


}
