package com.vip.zookeeper.javaapi;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

public class CreateSessionDemo implements Watcher {

	private final static String Connecting = "localhost:2181";
	// 计数器
	private static CountDownLatch countDownLatch = new CountDownLatch(1);

	@Test
	public void getTest() throws Exception {
		ZooKeeper zookeeper = new ZooKeeper(Connecting, 5000, this);
		countDownLatch.await();
		System.out.println(zookeeper.getState());
		// 创建节点， EPHEMERAL是临时节点
		String nodeName = zookeeper.create("/xdm", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println(nodeName);
		// 向zookeeper注册一个节点，watcher必须是true，标识持续监听，false不能注册
		zookeeper.getData("/xdm", true, new Stat());
		// 获取节点
		byte[] data = zookeeper.getData("/xdm", true, new Stat());
		System.out.println(new String(data));

		// 修改
		Stat setData = zookeeper.setData("/xdm", "890".getBytes(), -1);
		byte[] data2 = zookeeper.getData("/xdm", true, new Stat());
		System.out.println(new String(data2));

		// 删除节点
		// zookeeper.delete("/xdm", -1);
		// 永久节点
		String nodeName2 = zookeeper.create("/mic2", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		// 创建子节点
		String nodeName3 = zookeeper.create(nodeName2 + "/tom", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);
		System.out.println(nodeName3);

		// 判断节点是否存在
		Stat exists = zookeeper.exists("/xdm", true);
		if (exists == null) {// 如果不存在节点

		}
		
		//获取子节点
		List<String> children = zookeeper.getChildren("/mic", true);
		System.out.println(children);

		
	}

	@Override
	public void process(WatchedEvent watchedEvent) {
		System.out.println("执行watcher的process方法======start==========");
		/**
		 * 如果当前的状态是连接成功的，那么通过计数器去控制
		 **/
		if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
			countDownLatch.countDown();
			System.out.println(watchedEvent.getState());
		}
		System.out.println("执行watcher的process方法======end==========");
		// watcher对节点变化进行监听
		if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
		} else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
		} else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
		} else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
		} else if (watchedEvent.getType() == Event.EventType.None) {
		}
	}

}
