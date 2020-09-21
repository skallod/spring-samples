package ru.galuzin.zookeeper_reader;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.KeeperException;

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
        String str = "/graviy/test/value1";

        //sync , garantie
        AtomicInteger rc = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);
        try {
            client.getZookeeperClient().getZooKeeper().sync(str ,(code, path, ctx) ->{
                rc.set(code);
                latch.countDown();
            } , null);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        latch.await();
        KeeperException.Code code = KeeperException.Code.get(rc.get());
        if (code != KeeperException.Code.OK) {
            throw new RuntimeException("Ошибка при синхронизации пути " + str + ". Код ошибки: " + code);
        }

        byte[] bytes = client.getData().forPath(str);
        String s = new String(bytes);
        System.out.println("s = " + s);
        client.close();
    }
}
