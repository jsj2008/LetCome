package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductSellOrder extends BaseRes{

	private static final long serialVersionUID = 5755417123828677336L;
	
	public String result;
	public int order_id;
	
	public static class Params implements Serializable {
		
		private static final long serialVersionUID = -4347086402018340357L;
		
		public int p_id; //订单id
		public long stock_amount; // 股数
		public float start_price;
		public String sell_way = "1";
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack,15);//15秒超时
		proxy.getRequest(RequestInfo.PRODUCT_SELL_ORDER, params, ProductSellOrder.class, RETURN_TYPE, false);
	}

}
