package com.gxq.tpm.mode;

public abstract class ProductPreCheck extends BaseRes {
	private static final long serialVersionUID = 3494468550419011707L;

	public final static int AUTH 		= 1;
	public final static int RISK_NOTICE = 2; // 风险提醒
	public final static int RISK_INFORM = 3; // 风险告知
	public final static int CONTINUE_NORMAL	= 4; // 继续/取消确认框（无警告图标）
	public final static int CONTINUE_ALERT	= 5; // 继续/取消确认框（有警告图标）
	public final static int GO_TO_RECHARGE	= 6; // 去充值弹框
	public final static int AGREEMENT	= 10;
	public final static int NICKNAME	= 20;
	
	public String result; // 提交结果 'Y':通过/'N:不通过
	public int reason; // 弹框类型 0：不弹框 1：去认证弹框 2：风险确认弹框（要用msg_id进行风险确认）3：亏损过多确认框 4：继续/取消确认框（无警告图标） 5：继续/取消确认框（有警告图标） 6：去充值弹框 7：再看看/继续确认框 10：去获取未签署合约列表 20：去设置昵称
	public String msg_id; // msg_id
	public String msg; // 提示框信息

}
