package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.network.RequestInfo;

public class ProductSellStatus extends BaseRes {

	private static final long serialVersionUID = -309796526772355206L;

	public int state;	// 进度 1:委托中，2失败，3成功，4没有这笔订单
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = -24814201745757105L;
		
		int order_id;
	}
	
	public static void doRequest(int id, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.order_id = id;
		proxy.getRequest(RequestInfo.PRODUCT_SELL_STATUS, params, ProductSellStatus.class, RETURN_TYPE, false);
	}
	
}
