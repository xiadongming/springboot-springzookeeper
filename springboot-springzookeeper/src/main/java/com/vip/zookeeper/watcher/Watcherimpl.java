package com.vip.zookeeper.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class Watcherimpl implements Watcher{

	@Override
	public void process(WatchedEvent event) {

		if(event.getType() == Event.EventType.NodeCreated) {
			TestDemoWatcher.latch.countDown();
			System.out.println("监听到节点创建事件");
		}else if(event.getType() == Event.EventType.None) {
			TestDemoWatcher.latch.countDown();
			System.out.println("Event.EventType.None事件");
		}
		
		
	}

}
