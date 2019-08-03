package com.vip.zookeeper.curatorcache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import com.vip.zookeeper.curator.CuratorUtils;

public class TreeCacheDemo {
	
	
	@Test
	public void getTest() throws Exception {
		CuratorUtils curatorUtils = new CuratorUtils();
		CuratorFramework curatorFramework = curatorUtils.getInstance();
		curatorFramework.start();
	
		TreeCache treeCache = new TreeCache(curatorFramework, "/mic");
		treeCache.start();
		
		treeCache.getListenable().addListener(new TreeCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				
				switch(event.getType()) {
					case NODE_ADDED:
						System.out.println("NODE_ADDED事件");
						break;
					case NODE_UPDATED:
						System.out.println("NODE_UPDATED事件");
						break;
					case NODE_REMOVED:
						System.out.println("NODE_REMOVED事件");
						break;
					default:
						break;
				}
			}
		});
		//创建节点
		curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/mic","1234".getBytes());
        Thread.sleep(2000);		
		
        //修改节点
        curatorFramework.setData().forPath("/mic","890".getBytes());
        Thread.sleep(2000);
       //添加子节点
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/mic/tom","000".getBytes());
        Thread.sleep(2000);
        
        //删除节点
        curatorFramework.delete().forPath("/mic/tom");
        Thread.sleep(2000);
		
		
	}

}
