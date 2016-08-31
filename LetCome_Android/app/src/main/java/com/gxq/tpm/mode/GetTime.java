package com.gxq.tpm.mode;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetTime extends BaseRes {

	private static final long serialVersionUID = -3711135166444922382L;
	
	public long time;

	public static void doRequest(ICallBack netBack) {
		doRequest(netBack, RETURN_TYPE);
	}
	
	public static void doRequest(ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.GET_TIME, null, GetTime.class, tag, true);
	}
	
}
