package com.gxq.tpm.mode.mine;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetMyCustomerService extends BaseRes {
	private static final long serialVersionUID = 1482856452881576441L;
	
	public String nickname;
	public String url;
	public String desc;
	public String pic;

	public static void doRequest(ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
//		proxy.getRequest(RequestInfo.GET_MY_CUSTOMER_SERVICE.getOperationType(), ServiceConfig.getServiceUser(), 
//				null, InspGetMyCustomerService.class, null, false);
		proxy.getRequest(RequestInfo.GET_MY_CUSTOMER_SERVICE, null,
				GetMyCustomerService.class, RETURN_TYPE, false);
	}
	
}
