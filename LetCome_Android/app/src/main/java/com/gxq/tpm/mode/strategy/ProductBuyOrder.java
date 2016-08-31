package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductBuyOrder extends BaseRes {

	private static final long serialVersionUID = -8076468597864808028L;
	
	public int order_id;
	public int stock_amount;
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = 5020247471808087441L;
		
		public String p_id; // 策略ID
		public int buy_way = 1; // 点买方式 1:市价买入
		public double fund; // 申购金额
		public int stock_amount; // 申购股数
		public float start_price; // 价格
	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.PRODUCT_BUY_ORDER, params, 
				ProductBuyOrder.class, RETURN_TYPE, false);
	}
	
}
