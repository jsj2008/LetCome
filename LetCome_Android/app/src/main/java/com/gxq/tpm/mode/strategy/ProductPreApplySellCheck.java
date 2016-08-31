package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.ProductPreCheck;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.network.RequestInfo;

public class ProductPreApplySellCheck extends ProductPreCheck {

	private static final long serialVersionUID = 6839248335644277535L;

	public static class Params implements Serializable {

		private static final long serialVersionUID = 3042430417035907440L;
		
		public int p_id;
	}
	
	public static void doRequest(int pid, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.p_id = pid;
		proxy.getRequest(RequestInfo.PRODUCT_PRE_APPLY_SELL_CHECK, params,
				ProductPreApplySellCheck.class, RETURN_TYPE, false);
	}
}
