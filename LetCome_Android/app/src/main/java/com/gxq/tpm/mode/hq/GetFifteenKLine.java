package com.gxq.tpm.mode.hq;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetFifteenKLine extends GetKLine {

	private static final long serialVersionUID = 1172617346632993374L;

	public static void doRequest(String code, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.opt = RequestInfo.GET_15K.getOperationType();
		params.name = code;
		params.type = GlobalConstant.STOCK_HQ_TYPE;
		proxy.getHQRequest(RequestInfo.GET_15K, params, GetFifteenKLine.class, RETURN_TYPE, true);
	}
	
}
