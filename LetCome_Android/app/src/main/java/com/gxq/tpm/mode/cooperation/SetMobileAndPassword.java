package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class SetMobileAndPassword extends BaseRes{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -189639561627465062L;
	public String result;

	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -643983963292220566L;
		public String mobile;//	手机号	int	TRUE
		public String password; //	密码	sha1	TRUE
		public String mobile_code; //	手机验证码	string	TRUE
		public String nickname;	
	}

	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
		proxy.getRequest(RequestInfo.SET_MOBILE_AND_PASSWORD, params, SetMobileAndPassword.class, RETURN_TYPE, false);
   	}
}
