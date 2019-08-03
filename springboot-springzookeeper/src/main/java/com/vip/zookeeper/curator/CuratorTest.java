package com.vip.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorTest {
	private static final String CONNECTING = "127.0.0.1:2181";

	public static void main(String[] args) {

		// Curator创建连接的两种方式
		// 第一种：normal
		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTING, 5000, 5000,
				//重试策略
				new ExponentialBackoffRetry(1000, 3));
		
		curatorFramework.start();// 创建连接
		
		
		// 第二种fluent风格（链式风格）
		CuratorFramework curatorFramework2 = CuratorFrameworkFactory.builder().connectString(CONNECTING)
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		
		curatorFramework2.start();
		System.out.println("success");

	}
}
