package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class CheckPwd extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = -8530596860904842978L;
    
    public static class Params implements Serializable {
        /**
		 * 
		 */
        private static final long serialVersionUID = -7761597142642366945L;
        public String password;
    }
    
    public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);

   		proxy.getRequest(RequestInfo.PWD_CHECK, params, CheckPwd.class, RETURN_TYPE, false);
   	}
   
}
