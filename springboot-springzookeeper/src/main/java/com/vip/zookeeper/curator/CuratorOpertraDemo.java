package com.vip.zookeeper.curator;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

public class CuratorOpertraDemo {
	
	@Test
	public void getTest() throws Exception {
		CuratorUtils curatorUtils = new CuratorUtils();
		CuratorFramework curatorFramework = curatorUtils.getInstance();
		//创建连接
		curatorFramework.start();
		
     /*   //创建节点
		String result = curatorFramework.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT)
		.forPath("/curator/mic","123".getBytes());
		System.out.println(result);
		
		
		//删除节点 ,,,,可以删除子节点
		Void forPath2 = curatorFramework.delete().deletingChildrenIfNeeded().forPath("/curator/mic");
		System.out.println(forPath2);
		
		
		//获取数据
		byte[] forPath3 = curatorFramework.getData().forPath("/mic2");
		System.out.println(new String(forPath3));
		//获取数据,和stat信息
		Stat stat = new Stat();
		byte[] forPath4 = curatorFramework.getData().storingStatIn(stat).forPath("/mic2");
		System.out.println(new String(forPath4)+"===="+stat);
		
		
		//修改数据
		Stat forPath5 = curatorFramework.setData().forPath("/mic2","890".getBytes());
		System.out.println(forPath5);
		
		
		*/
		
		
		//异步操作 
/*		ExecutorService newFixedThreadPool =Executors.newFixedThreadPool(5);
		//创建计数器，初始值为1
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
		
		.inBackground(new BackgroundCallback() {
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println(Thread.currentThread().getName()+"=="+event.getResultCode()+"=="+event.getType());
				//计数器减一
				countDownLatch.countDown();
				
			}
			 //线程池
		},newFixedThreadPool).forPath("/mic2");
		//计数器不为0时，一直等待，当计数器为0时，所有线程并发执行任务
		countDownLatch.await();
		newFixedThreadPool.shutdown();
		*/
		
		//事务操作，curator独有
		Collection<CuratorTransactionResult> commit = curatorFramework.inTransaction().create().forPath("/mic1000").and()
		.setData().forPath("/mic100","678".getBytes()).and().commit();
		for (CuratorTransactionResult curatorTransactionResult : commit) {
			System.out.println(curatorTransactionResult);
		}
		
		
	}

}
