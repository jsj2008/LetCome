package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ResetPwd extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = 4837434546632841646L;
	public String result;//Y:成功N:失败

	public static class Params implements Serializable {
        /**
		 * 
		 */
        private static final long serialVersionUID = -3361498386454598038L;
        public String mobile;//		String
        public String password;//		String
//        public int type;//	1-一级密码 2-二级密码 3-三级密码	Int
	}

	public static void doRequest( Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//
//		proxy.postRequest(RequestInfo.FORGET_RESET_PWD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params),
//				ResetPwd.class, null, false, false);
		proxy.getRequest(RequestInfo.FORGET_RESET_PWD, params, ResetPwd.class, RETURN_TYPE, false);
	}
	
}
