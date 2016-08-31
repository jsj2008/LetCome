package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ConfirmRisk extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4601885661272281042L;
	
	public String result;

	public static final class Params implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6650498894819518151L;
	
		public String msg_id;
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.CONFIRM_RISK, params, ConfirmRisk.class, RETURN_TYPE, false);
	}
	
}
