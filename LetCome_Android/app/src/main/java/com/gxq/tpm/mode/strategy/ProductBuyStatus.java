package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductBuyStatus extends BaseRes {

	private static final long serialVersionUID = 1132293943712104593L;
	
	public int state; 	// 进度 1:委托中，2失败，3成功，4没有这笔订单
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = 8121684535833805776L;
		
		public int order_id;
	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.PRODUCT_BUY_STATUS, params, ProductBuyStatus.class,
				RETURN_TYPE, false);
	}
	
}
