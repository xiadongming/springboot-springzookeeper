package com.vip.zookeeper.watcher;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

public class TestDemoWatcher {
	
	public static final String CONNECTING = "127.0.0.1:2181";

	public static final int SESSIONTIMEOUT = 5000;
	
	public static CountDownLatch latch = new CountDownLatch(1);
	@Test
	public void getTest() {

		try {
			ZooKeeper zooKeeper = new ZooKeeper(CONNECTING,SESSIONTIMEOUT,new Watcherimpl());
			String create = zooKeeper.create("/laopo", "na".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			Thread.sleep(3000);
			latch.await();
            System.out.println(create);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
