//package ru.galuzin.article_1;
//
//import org.I0Itec.zkclient.IZkDataListener;
//import org.I0Itec.zkclient.exception.ZkNoNodeException;
//import org.I0Itec.zkclient.exception.ZkNodeExistsException;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
///**
// * Basic implementation of distributed locks
// *
// * There are two main methods:
// * releaseLock release lock
// * attemptLock attempts to acquire a lock
// *
// * Declaration:
// * Suppose there is a node node_c /node_a/node_b/node_c
// * Then, the name of the node_c node is node_c; the path of the node_c node is /node_a/node_b/node_c
// *
// * @author JustryDeng
// * @date 2018/12/6 16:13
// */
//public class BaseDistributedLock {
//
//    /** ZkClient client */
//    private final MyZkClient client;
//
//    /** /locker node path */
//    private final String lockerNodePath;
//
//    /**
//     * The current client's child node path under the /locker node
//     * Note: After creating a node, this path is not the path generated after the node is created. Because it is an ordered node, it will be slightly different.
//     *
//     * For example, if the current node path is /aspire/abc, then after creating the [temporary ordered] node, the actual path is /aspire/abc0000000001
//     */
//    private final String currentNodePath;
//
//    /**
//     * The node name of the child node of the current client under the /locker node
//     * Note: After creating a node, this name is not the name generated after the node is created. Because it is an ordered node, it will be slightly different.
//     *
//     * For example, if the current node name is abc, then after creating the [temporary ordered] node, the actual name is abc0000000001
//     */
//    @SuppressWarnings("all")
//    private final String currentNodeName;
//
//    /** Number of retries when the network is flashing */
//    private static final Integer MAX_RETRY_COUNT = 10;
//
//    /**
//     * Constructor
//     */
//    public BaseDistributedLock(MyZkClient client, String lockerNodePath, String currentNodeName) {
//        this.client = client;
//        this.lockerNodePath = lockerNodePath;
//        this.currentNodePath = lockerNodePath.concat("/").concat(currentNodeName);
//        this.currentNodeName = currentNodeName;
//    }
//
//    /**
//     * Release lock
//     *
//     * @author JustryDeng
//     * @date 2018/12/6 17:33
//     */
//    protected void releaseLock(String nodePath) {
//        deleteNode(nodePath);
//    }
//
//
//    /**
//     * Try to acquire a lock
//     *
//     * @param time
//     * Maximum waiting time
//     * @param unit
//     * Time unit of maximum waiting time
//     *
//     * return successfully acquires the lock, then returns the node path obtained after the current client creates the node.
//     * returns no lock, then returns null
//     * @date 2018/12/6 17:33
//     */
//    protected String attemptLock(long time, TimeUnit unit) throws Exception {
//
//        final long startMillis = System.currentTimeMillis();
//        final Long millisToWait = (unit != null) && (time != -1) ? unit.toMillis(time) : null;
//        String finalCurrentNodePath = null;
//        boolean gotTheLock = false;
//
//        boolean isDone = false;
//        int retryCount = 0;
//        // The first time you enter, the following code block will be entered; but when the network flashes, it will loop retry.
//        while (!isDone) {
//            isDone = true;
//            try {
//                try {
//                    // Create a temporary ordered child node
//                    finalCurrentNodePath = createEphemeralSequentialNode(currentNodePath, null);
//                } catch (ZkNoNodeException e) {
//                    // If there is a parent node that does not exist, then the parent node is created first, and the parent node path is: lockerNodePath
//                    client.createPersistent(lockerNodePath, true);
//                    // Create a temporary ordered child node again
//                    createEphemeralSequentialNode(currentNodePath, null);
//                } catch (ZkNodeExistsException e) {
//                    // Ignore this step multiple times due to network flashing, then ignore
//                }
//                gotTheLock = waitToLock(startMillis, millisToWait, finalCurrentNodePath);
//            } catch (ZkNoNodeException e) {
//                if (retryCount++ < MAX_RETRY_COUNT) {
//                    isDone = false;
//                } else {
//                    throw e;
//                }
//            }
//        }
//        if (gotTheLock) {
//            return finalCurrentNodePath;
//        }
//        return null;
//    }
//
//    /**
//     * Create temporary ordered nodes
//     */
//    private String createEphemeralSequentialNode(final String path, final Object data) {
//        return client.createEphemeralSequential(path, data);
//    }
//
//    /**
//     * Delete node
//     */
//    private void deleteNode(String nodePath) {
//        client.delete(nodePath);
//    }
//
//    /**
//     * Waiting for a lock
//     *
//     * @param startMillis
//     * Waiting for start time
//     * @param millisToWait
//     * Maximum waiting time
//     * @param finalCurrentNodePath
//     * The node created on the zookeeper corresponding to the current client
//     * @return whether the lock was successfully obtained
//     * @throws  Exception
//     *
//     * @date 2018/12/6 18:14
//     */
//    private boolean waitToLock(long startMillis, Long millisToWait, String finalCurrentNodePath) throws Exception {
//        boolean gotTheLock = false;
//        boolean doDelete = false;
//        try {
//            while (!gotTheLock) {
//                // Get all child nodes sorted by node name under the /locker node
//                List<String> children = getSortedChildren();
//                                 // Get the node name of the node corresponding to the current client
//                String sequenceNodeName = finalCurrentNodePath.substring(lockerNodePath.length() + 1);
//                // Get the location in the collection where the node corresponding to the current client is located
//                int ourIndex = children.indexOf(sequenceNodeName);
//                if (ourIndex < 0) { // throws an exception if the node does not exist in the collection
//                    throw new ZkNoNodeException("node not found: " + sequenceNodeName);
//                }
//                // When the node corresponding to the current client is at the beginning of the collection, it means that the client gets the lock.
//                boolean shouldGetTheLock = ourIndex == 0;
//                // The name of the node that the current client should monitor
//                String nodeNameToWatch = shouldGetTheLock ? null : children.get(ourIndex - 1);
//                if (shouldGetTheLock) {
//                    gotTheLock = true;
//                } else {
//                    // assemble the path of the node that the current client should monitor
//                    String previousSequencePath = lockerNodePath.concat("/").concat(nodeNameToWatch);
//                    // countdown lock
//                    final CountDownLatch latch = new CountDownLatch(1);
//                    // Create a listener
//                    final IZkDataListener previousListener = new IZkDataListener() {
//                        public void handleDataDeleted(String dataPath) {
//                            latch.countDown();
//                        }
//                        public void handleDataChange(String dataPath, Object data) {
//                            // ignore
//                        }
//                    };
//
//                    try {
//                                                 // If the node does not exist, an exception will occur
//                        client.subscribeDataChanges(previousSequencePath, previousListener);
//
//                        If (millisToWait != null) {// If the waiting time is set, then only wait for this long time
//                            millisToWait -= (System.currentTimeMillis() - startMillis);
//                            startMillis = System.currentTimeMillis();
//                            If (millisToWait <= 0) { // If the wait has timed out, then you need to delete the temporary ordered node corresponding to the current client.
//                                doDelete = true;
//                                break;
//                            }
//                            // CountDownLatch#await
//                            latch.await(millisToWait, TimeUnit.MICROSECONDS);
//                        } else { // If no wait time is set, then wait until you know that you have acquired the lock
//                            // CountDownLatch#await
//                            latch.await();
//                        }
//                    } catch (ZkNoNodeException e) {
//                        //ignore
//                    } finally {
//                        client.unsubscribeDataChanges(previousSequencePath, previousListener);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // An exception occurs and the node needs to be deleted
//            doDelete = true;
//            throw e;
//        } finally {
//            // If you need to delete a node
//            if (doDelete) {
//                deleteNode(finalCurrentNodePath);
//            }
//        }
//        return gotTheLock;
//    }
//
//
//    /**
//     * Sort by ascending node name, ascending
//     *
//     * @date 2018/12/6 18:14
//     */
//    private List<String> getSortedChildren() {
//        try {
//            List<String> children = client.getChildren(lockerNodePath);
//            children.sort(Comparator.comparing(String::valueOf));
//            return children;
//        } catch (ZkNoNodeException e) {
//            client.createPersistent(lockerNodePath, true);
//            return getSortedChildren();
//        }
//    }
//
//}