package com.vip.zookeeper.master;

import java.io.Serializable;

public class UserCenter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2354109467198354273L;

	private int mc_id ;
	private String mc_name;
	public int getMc_id() {
		return mc_id;
	}
	public void setMc_id(int mc_id) {
		this.mc_id = mc_id;
	}
	public String getMc_name() {
		return mc_name;
	}
	public void setMc_name(String mc_name) {
		this.mc_name = mc_name;
	}
	
	
	
	
	
	
}
