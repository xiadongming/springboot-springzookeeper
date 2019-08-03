package com.vip.zookeeper.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

public class TestZKClient {

	private static final String CONNECTING = "127.0.0.1:2181";

	@Test
	public void getTest() throws InterruptedException {

		ZkClient zkClient = new ZkClient(CONNECTING, 5000);
		zkClient.createPersistent("/jack2", true);
		// zkClient提供递归创建节点
		zkClient.createPersistent("/mic/tom/jack", true);

		// 删除
		zkClient.delete("/mic/tom/jack");
		//递归删除
		zkClient.deleteRecursive("/mic/tom");

		//获取子节点
		zkClient.getChildren("/mic");
		
		System.out.println("success");
	
	   //zkClient的订阅消息(监听消息)
		zkClient.subscribeDataChanges("/jack2", new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("节点删除");
			}
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("消息订阅=="+dataPath+"//////"+data);	
			}
		});
		zkClient.writeData("/jack2", "100000");
		Thread.sleep(1000);//睡一秒中才能监听到
		zkClient.delete("/jack2");
		Thread.sleep(1000);
		/**
		zkClient.subscribeChildChanges(path, listener);
		zkClient.subscribeStateChanges(listener);
		zkClient.subscribeDataChanges(path, listener);
	   **/
	}

}
