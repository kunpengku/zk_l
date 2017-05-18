package zk_learn;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fupeng on 2017/5/18.
 */
public class GetChildrenASync implements Watcher {

    private static CountDownLatch countdownLatch = new CountDownLatch(1);
    private  static ZooKeeper zk =null;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        System.out.println("1");

        zk = new ZooKeeper("10.60.0.26:2181,10.60.0.26:2183,10.60.0.26:2185",
                5000, new GetChildrenASync());

        System.out.println(zk.getState());
        countdownLatch.await();

        System.out.println("zk established");
        String path = "/fp";

        String path1 = zk.create(path, "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        String path2 = zk.create(path+"/c1", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zk.getChildren(path,true, new IChildren2Callback(), null);

        zk.create(path+"/c2", "9005".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        Thread.sleep(5000);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countdownLatch.countDown();
                countdownLatch.countDown();
            } else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("Re Get Child:" + zk.getChildren(watchedEvent.getPath(),true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

class IChildren2Callback implements AsyncCallback.Children2Callback{

    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        System.out.println("Get znode result:" + list);
    }
}
