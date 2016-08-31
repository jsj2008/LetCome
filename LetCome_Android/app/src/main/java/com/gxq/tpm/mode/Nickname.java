package com.gxq.tpm.mode;

import java.io.Serializable;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class Nickname extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String result;// Y:成功N:失败

	public static class Params implements Serializable {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;
		public String nickname;// 昵称
	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//
//		proxy.postRequest(RequestInfo.SET_NICKNAME.getOperationType(),
//				ServiceConfig.getServiceUser(), gson.toJson(params),
//				Nickname.class, null, false, false);
		proxy.getRequest(RequestInfo.SET_NICKNAME, params, Nickname.class, RETURN_TYPE, false);
	}
}
