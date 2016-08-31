
package com.gxq.tpm.network;
public enum NetworkResultInfo {
	SESSION_TIMEOUT(110006, "session过期"),
//	CARD_INFO_ERR(1811,"卡信息错"),
//	SKIP_TIME_OVER(1902,"免输支付密码已用完"),
//	REPEAT_PAY_ERR(1809,"重复支付"),
//	ORDER_OUT_TIME_ERR(1808,"订单已过期"),
//	TOKEN_OUT_TIME(1043,"token过期"),
	WITH_UNSIGNED_AGREEMENT(74000, "存在未签署的协议"),
	WITH_UNSIGNED_FRAME_AGREEMENT(74016, "存在未签署的框架协议"),
	SET_NICKNAME(30019, "昵称未设置"),
	CHARGE_NOT_ENOUGH(30014, "操盘宝余额不足"),
	POINT_NOT_ENOUGH(30023, "积分不足"),
	VIRTUAL_BUY_FIVE_AT_MOST(30035, "模拟交易最多持仓"), 
	BUY_AMOUNT_NOT_MATCH(30009, "股数数目错误"),
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
