package com.gxq.tpm.mode.mine;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetSmsVerify extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 256893537889309960L;
	
	public String result;//Y验证成功，N验证失败

	public static void doRequest(ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.MOBILE_SEND_CODE_FOR_AUTH, null, GetSmsVerify.class, RETURN_TYPE, false);
   	}

}
