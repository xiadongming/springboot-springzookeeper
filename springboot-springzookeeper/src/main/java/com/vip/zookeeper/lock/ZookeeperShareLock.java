package com.vip.zookeeper.lock;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperShareLock {

	private static final String ROOT_LOCAK = "/LOCKS2";// 根节点

	private ZooKeeper zookeeper;

	private int SessionTimeOut;

	private String LocakID;// 记录锁节点id

	private static final byte[] bytedata = { 1, 2 };

	private CountDownLatch countDownLatch = new CountDownLatch(1);

	public ZookeeperShareLock() throws IOException, InterruptedException {
		this.zookeeper = ZKClient.getInstance();
		this.SessionTimeOut = ZKClient.getSessionTimeOut();
	}

	// 获取所锁方法
	public boolean lock() {
		// 节点名称 //没有acl权限 //临时有序节点
		try {
			LocakID = zookeeper.create(ROOT_LOCAK + "/", bytedata, ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			System.out.println(Thread.currentThread().getName() + "成功创建节点" + LocakID + "开始去竞争锁");
			// 获取根节点下所有的子节点
			List<String> children = zookeeper.getChildren(ROOT_LOCAK, true);
			// 排序，从小到大
			SortedSet<String> treeSet = new TreeSet<String>();
			for (String childr : children) {
				treeSet.add(ROOT_LOCAK + "/" + childr);
			}
			String first = treeSet.first();
			
			// 就是最小的节点，
			if (LocakID.equals(first)) {
				System.out.println(Thread.currentThread().getName() + "成功获取锁，节点为" + LocakID);
				return true;
			}
			// 如果不是最小的节点,,,需要监控比他LocakID小的上一个节点，当上一个节点断掉时，LocakID上位
			// 获取LocakID元素前面的元素的
			SortedSet<String> headSet = treeSet.headSet(LocakID);
			if (!headSet.isEmpty()) {
				String lastNode = headSet.last();
				// 如果last节点删除的watcher监听
				zookeeper.exists(lastNode, new LockWatcher(countDownLatch));
				countDownLatch.await(SessionTimeOut, TimeUnit.MILLISECONDS);// 毫秒
				// 则LocakID获取锁成功
				System.out.println(Thread.currentThread().getName() + "成功获取锁，节点为" + LocakID);
			}
			return true;
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean unlock() {
		try {
			zookeeper.delete(LocakID, -1);
			System.out.println(LocakID + "节点删除成功");
			return true;
		} catch (InterruptedException | KeeperException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		CountDownLatch Latch = new CountDownLatch(10);
		for (int a = 0; a < 10; a++) {
			new Thread(() -> {
				ZookeeperShareLock lock = null;
				try {
					lock = new ZookeeperShareLock();
					Latch.countDown();
					Latch.await();
					lock.lock();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (lock != null) {
						lock.unlock();
					}
				}
			}).start();
		}
	}

}
