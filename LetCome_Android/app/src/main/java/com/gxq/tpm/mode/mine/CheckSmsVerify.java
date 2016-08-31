package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class CheckSmsVerify extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -66959596647457263L;

	public String result;

	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6967432474133185836L;
		public String mobile_code;
	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.MOBILE_CHECK_MOBILE_CODE, params, CheckSmsVerify.class, RETURN_TYPE, false);
	}

}
