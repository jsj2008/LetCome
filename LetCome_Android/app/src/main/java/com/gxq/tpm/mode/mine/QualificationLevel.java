package com.gxq.tpm.mode.mine;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class QualificationLevel extends BaseRes {	

	private static final long serialVersionUID = 9109028739306961313L;
	
	public String name;
	
	public static void doRequest(ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.QUALIFICATION_LEVEL, null, QualificationLevel.class, RETURN_TYPE, false);
	}

}
