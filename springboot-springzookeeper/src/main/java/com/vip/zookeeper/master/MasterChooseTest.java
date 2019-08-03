package com.vip.zookeeper.master;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class MasterChooseTest {
	private static final String CONNECTING = "127.0.0.1:2181";

	public static void main(String[] args) {
		ArrayList<MasterSelect> arrayList = new ArrayList<>();
		try {
			for (int i = 0; i < 10; i++) {

				ZkClient zkClient = new ZkClient(CONNECTING, 5000, 5000, new SerializableSerializer());

				UserCenter userCenter = new UserCenter();
				userCenter.setMc_id(i);
				userCenter.setMc_name("客户端：" + i);
				MasterSelect masterSelect = new MasterSelect(userCenter,zkClient);
				arrayList.add(masterSelect);
				masterSelect.start();
				TimeUnit.SECONDS.sleep(4);

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			for (MasterSelect masterSelect : arrayList) {
				masterSelect.stop();
			}
		}
	}
}
