package ru.galuzin.zookeeper.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ZookeeperHelper {

    public static void sync(CuratorFramework client, String str) throws InterruptedException {
        //sync , garantie
        AtomicInteger rc = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);
        try {
            client.getZookeeperClient().getZooKeeper().sync(str,(code, path, ctx) ->{
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
    }

    public static DataWithVersion readValue(CuratorFramework client, String str) throws Exception {
//        byte[] bytes = client.getData().forPath(str);
//        String s = new String(bytes);

        Stat stat = new Stat();
        byte[] data = client.getZookeeperClient().getZooKeeper().getData(str, null, stat);
        //String s = new String(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int value = buffer.getInt();
        System.out.println("read s = " + value + " ; " + stat.getVersion());
        return new DataWithVersion(value, stat.getVersion());
    }

    public static boolean checkExist(CuratorFramework client, String str) throws Exception {
        return client.checkExists().forPath(str) != null;
    }

    public static void writeValue(CuratorFramework client, String key, int value , int version) throws Exception {
        //Stat stat = client.setData().forPath(str, "abra1".getBytes());
        final byte[] data = ByteBuffer.allocate(4).putInt(value).array();
        Stat stat = client.getZookeeperClient().getZooKeeper().setData(key, data, version);
        System.out.println("write result " + stat.getVersion());
    }

    public static void initPath (CuratorFramework client, String path) {
        try {
            if (!checkExist(client, path)) {
                client.create().creatingParentContainersIfNeeded().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class DataWithVersion {
        public int value;
        public int version;

        public DataWithVersion(int value, int version) {
            this.value = value;
            this.version = version;
        }
    }
}
