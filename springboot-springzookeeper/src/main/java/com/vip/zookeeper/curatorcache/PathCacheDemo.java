package com.vip.zookeeper.curatorcache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import com.vip.zookeeper.curator.CuratorUtils;

public class PathCacheDemo {
	
	
	@Test
	public void getTest() throws Exception {
		CuratorUtils curatorUtils = new CuratorUtils();
		CuratorFramework curatorFramework = curatorUtils.getInstance();
		curatorFramework.start();
	
		                                                                                  //是否缓存数据
		PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, "/mic", true);
		pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
		
		pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				Type type = event.getType();
				
              switch(type) {
                 
              case CHILD_ADDED:
            	  System.out.println("CHILD_ADDED事件");
              break;
              case CHILD_UPDATED:
            	  System.out.println("CHILD_UPDATED事件");
              break;
              case CHILD_REMOVED:
            	  System.out.println("CHILD_REMOVED事件");
              default:break;
              }				
			}
		});
		//创建节点
		curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/mic","789".getBytes());
		Thread.sleep(2000);
		
		//创建子节点
		curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/mic/tom","890".getBytes());
		Thread.sleep(2000);
		
		//修改数据,,,只有修改子节点的数据才能被监听到，修改父节点的数据监听不到
		curatorFramework.setData().forPath("/mic/tom","100".getBytes());
		 //修改数据有可能瞬间完成，使线程休息一会，让监听器监听到
		Thread.sleep(2000);
		
		//删除节点
		curatorFramework.delete().forPath("/mic/tom");
		curatorFramework.delete().forPath("/mic");
		Thread.sleep(2000);
		
	}

}
