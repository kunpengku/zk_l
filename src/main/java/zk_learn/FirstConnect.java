package zk_learn;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fupeng on 2017/5/18.
 */
public class FirstConnect implements Watcher {

    private static CountDownLatch countdownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("1");

        ZooKeeper zk = new ZooKeeper("10.60.0.26:2181,10.60.0.26:2183,10.60.0.26:2185",
                5000, new FirstConnect());

        System.out.println(zk.getState());
        countdownLatch.await();

        System.out.println("zk established");

    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            countdownLatch.countDown();
        }

    }
}
