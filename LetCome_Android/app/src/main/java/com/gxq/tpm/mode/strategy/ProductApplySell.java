package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductApplySell extends BaseRes {

	private static final long serialVersionUID = -797785236132499069L;

	public String result;
	
	public static class Params implements Serializable {
		private static final long serialVersionUID = 1489225007698828985L;

		int p_id;
	}
	
	public static void doRequest(int pid, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.p_id = pid;
		
		proxy.getRequest(RequestInfo.PRODUCT_APPLY_SELL, params, ProductApplySell.class, RETURN_TYPE, false);
	}
	
}
