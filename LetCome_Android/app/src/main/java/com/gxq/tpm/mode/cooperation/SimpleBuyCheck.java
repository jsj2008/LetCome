package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class SimpleBuyCheck extends BaseRes {

	private static final long serialVersionUID = 4291521698406161845L;

	public String bullish;
	public String error_reason_msg;
	public String error_reason;
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5766699438428971494L;
		
		public String stock_code;
		public String p_type;
		public int scheme_id;
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.PRODUCT_SIMPLE_BUY_CHECK, params, SimpleBuyCheck.class, RETURN_TYPE, false);
	}
	
}
