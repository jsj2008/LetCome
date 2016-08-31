package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class Verify extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = 3452577000203966735L;

    
    public static class Params implements Serializable{
        /**
		 * 
		 */
        private static final long serialVersionUID = 4329600944219393308L;
		public String code;//	验证码
		public String UUID;//	验证码KEY
    }
    
    public static void doRequest(Params params,ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		proxy.getRequest(RequestInfo.CHECK_VERIFY, params, Verify.class, RETURN_TYPE, false);
   	}
   
}
