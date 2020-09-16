package ru.galuzin.zookeeper_writer;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

public class ZookeeperWriterMain {

    public static void main(String[] args) throws Exception {
        int sleepMsBetweenRetries = 100;
        int maxRetries = 3;
        RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2182", retryPolicy);
        client.start();
        String str = "/graviy/test/value1";
        client.create().creatingParentContainersIfNeeded().forPath(str);
        client.setData().forPath(str, "abra1".getBytes());
        client.close();
    }
}
