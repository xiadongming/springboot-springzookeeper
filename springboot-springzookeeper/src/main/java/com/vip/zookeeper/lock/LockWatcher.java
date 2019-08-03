package com.vip.zookeeper.lock;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class LockWatcher implements Watcher {

	private CountDownLatch countDownLatch;

	public LockWatcher(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void process(WatchedEvent event) {

		if (event.getType() == Event.EventType.NodeDeleted) {
			countDownLatch.countDown();
		}

	}

}
