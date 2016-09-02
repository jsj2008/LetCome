
package com.gxq.tpm.network;
public enum NetworkResultInfo {
	NETWORK_ERROR(-1, ""),
	SERVICE_ERROR(-2, ""),
	SUCCESS(0, "成功");

	private int value;
	private String name;

	private NetworkResultInfo(int value,String name){
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
