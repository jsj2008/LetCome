package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class MineResetPwd extends BaseRes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6586530705871191676L;
	/**
	 * 
	 */
   
	public String result;//Y:成功N:失败

	public static class Params implements Serializable {
		/**
		 * 
		 */
        private static final long serialVersionUID = 2554566783185056978L;
        public long uid;//		Int
        public String PhonrNumber; //int
        public String passwordNew;//		String
	}

	public static void doRequest( Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.RESET_PWD, params, MineResetPwd.class, RETURN_TYPE, false);
	}
}
