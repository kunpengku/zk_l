package zk_learn;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fupeng on 2017/5/18.
 */
public class CreateNodeSync implements Watcher {

    private static CountDownLatch countdownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        System.out.println("1");

        ZooKeeper zk = new ZooKeeper("10.60.0.26:2181,10.60.0.26:2183,10.60.0.26:2185",
                5000, new CreateNodeSync());

        System.out.println(zk.getState());
        countdownLatch.await();

        System.out.println("zk established");

        String path1 = zk.create("/fp", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("success create node" + path1);

        String path2 = zk.create("/fp_ephemeral_sequ", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("success create node" + path2);

        String path3 = zk.create("/fp_persistent_sequ", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("success create node" + path3);

        Thread.sleep(5000);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            countdownLatch.countDown();
        }

    }
}
