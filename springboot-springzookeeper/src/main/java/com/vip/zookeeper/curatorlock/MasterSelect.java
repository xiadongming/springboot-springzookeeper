package com.vip.zookeeper.curatorlock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class MasterSelect {
	private final static String MASTER_PATH = "/LOCKS";// 需要争抢的节点
	private static final String CONNECTING = "127.0.0.1:2181";

	public static void main(String[] args) {
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTING)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, MASTER_PATH,
				new LeaderSelectorListenerAdapter() {

					// 只有成功选举位leader的节点，才能进入这个方法
					@Override
					public void takeLeadership(CuratorFramework client) throws Exception {
						System.out.println("获取leader选举成功");
						System.out.println(client);
					}
				});

		leaderSelector.autoRequeue();
		leaderSelector.start();// 开始选举
	}

}
