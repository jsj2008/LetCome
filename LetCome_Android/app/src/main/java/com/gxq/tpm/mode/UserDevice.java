package com.gxq.tpm.mode;

import java.io.Serializable;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class UserDevice extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9118989570560858586L;
	
	public String result;

	public static class Params implements Serializable {

		private static final long serialVersionUID = -6123807444231556947L;
		
		public String dev_id;
		public int dev_type;

	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//
//		proxy.postRequest(RequestInfo.USER_DEVICE.getOperationType(),
//				ServiceConfig.getServiceUser(),
//				gson.toJson(params), UserDevice.class, null, false, true);
		proxy.getRequest(RequestInfo.USER_DEVICE, params, UserDevice.class, RETURN_TYPE, false);
	}

}
