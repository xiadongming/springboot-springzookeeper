package com.vip.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorUtils {

	private static final String CONNECTING = "127.0.0.1:2181";
	
	public CuratorFramework getInstance() {
		
		return CuratorFrameworkFactory.newClient(CONNECTING, 5000,5000,
				new ExponentialBackoffRetry(1000, 3));
	}
	
	
}
