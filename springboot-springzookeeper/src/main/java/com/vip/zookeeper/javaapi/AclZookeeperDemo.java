package com.vip.zookeeper.javaapi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.Test;

public class AclZookeeperDemo implements Watcher {

	private final static String Connecting = "localhost:2181";
	// 计数器
	private static CountDownLatch countDownLatch = new CountDownLatch(1);

	@Test
	public void getTest() throws Exception {
		ZooKeeper zookeeper = new ZooKeeper(Connecting, 5000, this);
		countDownLatch.await();

		// 特定用户才能访问数据
		ACL acl1 = new ACL(ZooDefs.Perms.CREATE, new Id("digest", "root:root"));
		// 特定ip才能访问数据
		ACL acl2 = new ACL(ZooDefs.Perms.CREATE, new Id("ip", "127.0.0.1"));
		List<ACL> arrayList = new ArrayList<ACL>();
		arrayList.add(acl1);
		arrayList.add(acl2);
		zookeeper.create("/xdm", "123".getBytes(), arrayList, CreateMode.PERSISTENT);
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
