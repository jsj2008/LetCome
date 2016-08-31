package com.gxq.tpm.mode.account;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductDetails extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4588407453525765983L;
	
	public int p_id; //	点买人id	int
	public int id; //	产品id	Int
	public int uid; //	点买人id	int
	public String buy_date;	//开仓日期	String
	public int buy_times;	//策略买次数 int
	public int buy_amount;	//买入股数	int
	public float buy_fund;//买入成交金额
	public float buy_deal_avg_price;//	买入价	float
	public String apply_sell_date; //	通知平仓日	String
	public String apply_sell_days; //	通知平仓日所在日	String
	public float y_close;//	通知平仓日收盘价	String
	public float apply_sell_profit_rate; //	通知平仓日收益浮盈   String
	public long sell_date	; //平仓日期	String
	public int sell_times; //	策略卖次数	int
	public float sell_deal_avg_price; //	卖出价	float
	public int sell_amount;//	卖出股数	Int
	public float sell_fund;//卖出成交金额
	public int state;//状态 1:用户通知 2:系统通知

	
	public static class Distribution implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8549909294941340631L;
		public float user_profit; //用户分配比例
		public float t_profit;
		public float f_profit;
		public float p_profit;
	}
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -825578161210740937L;
		public int p_id;	//产品ID	
		public String p_type;	//产品类型	
	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.PRODUCT_DETAILS, params, ProductDetails.class, RETURN_TYPE, false);
	}

}
