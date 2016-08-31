package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.ProductPreCheck;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.network.RequestInfo;

public class ProductPreBuyCheck extends ProductPreCheck {

	private static final long serialVersionUID = -2079785008552217684L;

    public int stock_amount;
    public int error_reason;
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = 1217006697439431799L;

		public String p_id; // 策略ID
		public int buy_way = 1; // 点买方式 1:市价买入
		public double fund; // 申购金额
		public int stock_amount; // 申购股数
		public float start_price; // 价格
		public String stock_code; // 股票代码
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.PRODUCT_PRE_BUY_CHECK, params, ProductPreBuyCheck.class, RETURN_TYPE, false);
	}
	
}
