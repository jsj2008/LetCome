package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class CheckSmsVerifyNew extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = -982287675540277429L;
	
	public String result;
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8401735547011943164L;
		public String mobile;
		public String mobile_code;

	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.MOBILE_BIND_NEW_CODE, params, CheckSmsVerifyNew.class, RETURN_TYPE, false);
	}

}
