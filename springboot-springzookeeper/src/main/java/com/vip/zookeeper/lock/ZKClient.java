package com.vip.zookeeper.lock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZKClient {
	
	private static final String CONNECTING = "127.0.0.1:2181";

	private static int SessionTimeOut = 2000;
	
	public static ZooKeeper getInstance() throws IOException, InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		ZooKeeper zooKeeper = new ZooKeeper(CONNECTING,SessionTimeOut,new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				if(event.getState() == Event.KeeperState.SyncConnected) {
					countDownLatch.countDown();
				}
			}});
		countDownLatch.await();
		return zooKeeper;
	}
	
	public static int getSessionTimeOut() {
		return SessionTimeOut;
	}
}
