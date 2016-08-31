package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetSmsVerifyNew extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4436224500209024584L;
	
	public String result;//Y验证成功，N验证失败
	
	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -677440160384396831L;
		
		public String mobile;
		
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);

   		proxy.getRequest(RequestInfo.MOBILE_SEND_NEW_MOBILE_CODE, params, GetSmsVerifyNew.class, RETURN_TYPE, false);
   	}

}
