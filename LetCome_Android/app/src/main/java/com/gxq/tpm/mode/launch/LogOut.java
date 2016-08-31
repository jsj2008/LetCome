package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class LogOut extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = 5633641723262903671L;
	public String result;//Y:成功N:失败

	public static class Params implements Serializable {
		/**
		 * 
		 */
        private static final long serialVersionUID = -7163673988733587253L;
	}

	public static void doRequest( Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		proxy.getRequest(RequestInfo.LOGOUT.getOperationType(), ServiceConfig.getServiceUser()/*.replace("https", "http")*/, 
//				null, LogOut.class,null,false);
		proxy.getRequest(RequestInfo.LOGOUT, params, LogOut.class, RETURN_TYPE, false); 
	}
	
}
