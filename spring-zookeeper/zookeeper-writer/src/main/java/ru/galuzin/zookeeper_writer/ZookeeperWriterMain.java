package ru.galuzin.zookeeper_writer;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import ru.galuzin.zookeeper.common.ZookeeperHelper;

public class ZookeeperWriterMain {

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

        Thread.sleep(60_000);
        client.close();
    }


}
