package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ChangePwd extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = -7451464723457087766L;
	public String result;//Y:成功N:失败

	public static class Params implements Serializable {
		/**
		 * 
		 */
        private static final long serialVersionUID = 2554566783185056978L;
        public long uid;//		Int
        public String passwordNew;//		String
        public String passwordOld;//		String
        public int type;//	1-一级密码 2-二级密码 3-三级密码	Int

	}

	public static void doRequest( Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.CHANGE_PWD, params, ChangePwd.class, RETURN_TYPE, false);
	}
}
