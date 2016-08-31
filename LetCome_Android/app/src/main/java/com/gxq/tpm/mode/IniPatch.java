package com.gxq.tpm.mode;

import java.io.Serializable;
import java.util.Map;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class IniPatch extends BaseRes {

	private static final long serialVersionUID = -3614085370993763255L;
	
	public String result;
	public String is_update;
	public String ini_version;
	public Map<String, String> res_data;

	public static class Params implements Serializable {
		private static final long serialVersionUID = 7111347526875946815L;
		public String version;
		public String ini_version;
	}

	public static void doRequest(ICallBack netBack, Params params) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.UPDATE_INIPATH, params, IniPatch.class, RETURN_TYPE, false);
	}
	
}
