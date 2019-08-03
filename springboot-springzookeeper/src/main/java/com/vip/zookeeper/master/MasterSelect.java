package com.vip.zookeeper.master;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;



public class MasterSelect {

	private ZkClient zkClient;

	private final static String MASTER_PATH = "/master";// 需要争抢的节点

	private IZkDataListener IZKDataListener;// 节点内容变化

	private UserCenter server;// 其他服务器
	private UserCenter master;// master节点

	private static boolean isRunning = false;

	ScheduledExecutorService  scheduledExecutorService = Executors.newScheduledThreadPool(1);
	
	public MasterSelect(UserCenter server,ZkClient zkClient) {
		this.server = server;
		this.zkClient = zkClient;
		System.out.println("去争抢master权限");
		this.IZKDataListener = new IZkDataListener() {
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
			  System.out.println("触发节点删除事件");
			}
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// 主节点删除，发起选主操作
				chooseMaster();
			}
		};

	}

	public void start() {
		// 开始选举

		if(!isRunning) {
			isRunning = true;
			zkClient.subscribeDataChanges(MASTER_PATH, IZKDataListener);//注册节点事件
			chooseMaster();
		}
		
	}

	public void stop() {
		// 停止
		
		if(isRunning) {
			isRunning = false;
			scheduledExecutorService.shutdown();
			zkClient.unsubscribeDataChanges(MASTER_PATH, IZKDataListener);
			releaseMaster();
		}
	}

	public void chooseMaster() {
		// 开始选举

		if (!isRunning) {
			System.out.println("当前服务没有启动");
			return;
		}
		try {
		
		zkClient.createEphemeral(MASTER_PATH, server);
		master = server;
		System.out.println("客户端 ："+master.getMc_id()+"  "+"我现在是master，");
		//定时器
		scheduledExecutorService.schedule(()->{
			releaseMaster();
		}, 5, TimeUnit.SECONDS);
		
		
		}catch(ZkNodeExistsException e) {
			//标识master已经存在
			UserCenter userCenter = zkClient.readData(MASTER_PATH,true);
			if(userCenter == null) {
				chooseMaster();
			}else {
				master = userCenter;
			}
			
		}
		
	}

	public void releaseMaster() {
		// 释放锁,只有master才能释放锁
		if(checkIsMaster()) {
			zkClient.delete(MASTER_PATH);
			
		}

	}

	public boolean checkIsMaster() {

		// 判断是否是master
		UserCenter userCenter = zkClient.readData(MASTER_PATH);
		if(userCenter.getMc_name().equals(server.getMc_name())) {
			master = userCenter;
			return true;
		}
		return false;
	}
}
