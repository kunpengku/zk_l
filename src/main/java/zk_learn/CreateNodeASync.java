package zk_learn;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fupeng on 2017/5/18.
 */
public class CreateNodeASync implements Watcher {

    private static CountDownLatch countdownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        System.out.println("1");

        ZooKeeper zk = new ZooKeeper("10.60.0.26:2181,10.60.0.26:2183,10.60.0.26:2185",
                5000, new CreateNodeASync());

        System.out.println(zk.getState());
        countdownLatch.await();

        System.out.println("zk established");

        zk.create("/fp", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallback(), "I am Context");

        zk.create("/fp", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallback(), "I am Context");

        zk.create("/fp_persistent_sequ", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL, new IStringCallback(), "I am Context");

        Thread.sleep(5000);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            countdownLatch.countDown();
        }

    }
}

class IStringCallback implements AsyncCallback.StringCallback{

    public void processResult(int i, String s, Object o, String s1) {
        System.out.println("create path result: [" + i + "," + s + "," + o + "," + s1);
    }
}
