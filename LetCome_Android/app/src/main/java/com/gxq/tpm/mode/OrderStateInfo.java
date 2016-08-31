package com.gxq.tpm.mode;

import java.util.HashMap;

public final class OrderStateInfo {
	
	public static final int STATE_INVAILD = -1;
	public static final int STATE_0 = 0;
	public static final int STATE_1 = 1;
	public static final int STATE_2 = 2;
	public static final int STATE_3 = 3;
	public static final int STATE_4 = 4;
	public static final int STATE_5 = 5;
	public static final int STATE_6 = 6;
	public static final int STATE_7 = 7;
	public static final int STATE_8 = 8;
	public static final int STATE_9 = 9;
	public static final int STATE_10 = 10;
	public static final int STATE_11 = 11;
	public static final int STATE_12 = 12;
	public static final int STATE_13 = 13;
	public static final int STATE_14 = 14;
	public static final int STATE_15 = 15;
	public static final int STATE_16 = 16;
	public static final int STATE_17 = 17;
	public static final int STATE_18 = 18;
	public static final int STATE_19 = 19;
	public static final int STATE_20 = 20;
	public static final int STATE_21 = 21;
	
	private static final HashMap<Integer, String> STATE_VALUE = new HashMap<Integer, String>();
	
	static {
		STATE_VALUE.put(STATE_INVAILD, "正在查询");
		STATE_VALUE.put(STATE_0, "失败");
		STATE_VALUE.put(STATE_1, "正在申购");
		STATE_VALUE.put(STATE_2, "非点卖日");
		STATE_VALUE.put(STATE_3, "操作");
		STATE_VALUE.put(STATE_4, "系统接管");
		STATE_VALUE.put(STATE_5, "正在卖出");
		//STATE_VALUE.put(STATE_6, "正在查询");
		STATE_VALUE.put(STATE_7, "正在卖出");
		//STATE_VALUE.put(STATE_8, "正在查询");
		STATE_VALUE.put(STATE_9, "触发卖出");
		//STATE_VALUE.put(STATE_10, "正在查询");
		STATE_VALUE.put(STATE_11, "即时卖出");
		//STATE_VALUE.put(STATE_12, "正在查询");
		STATE_VALUE.put(STATE_13, "止盈中止");
		//STATE_VALUE.put(STATE_14, "正在查询");
		STATE_VALUE.put(STATE_15, "止损中止");
		//STATE_VALUE.put(STATE_16, "正在查询");
		STATE_VALUE.put(STATE_17, "到时中止");
		STATE_VALUE.put(STATE_18, "触发申报");
		STATE_VALUE.put(STATE_19, "待结");
		STATE_VALUE.put(STATE_20, "已结");
		STATE_VALUE.put(STATE_21, "过期");
	}
	
	public static final String getStateValue(int state) {
		return STATE_VALUE.get(state);
	}
	
}
