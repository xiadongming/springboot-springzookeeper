package com.vip.zookeeper.curatorcache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.junit.Test;

import com.vip.zookeeper.curator.CuratorUtils;

public class NodeCacheDemo {
	
	
	@Test
	public void getTest() throws Exception {
		CuratorUtils curatorUtils = new CuratorUtils();
		CuratorFramework curatorFramework = curatorUtils.getInstance();
		curatorFramework.start();
		                                                          //是否压缩数据
		NodeCache nodeCache = new NodeCache(curatorFramework,"/mic",false);
		 // 如果为true则首次不会缓存节点内容到cache中，默认为false,设置为true首次不会触发监听事件
		nodeCache.start(true);
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
					System.out.println("进行nodecache监听==="+new String(nodeCache.getCurrentData().getData()));
			}
			
		});
		//修改数据
		curatorFramework.setData().forPath("/mic","10000".getBytes());
		 //修改数据有可能瞬间完成，使线程休息一会，让监听器监听到
		Thread.sleep(2000);
	}

}
