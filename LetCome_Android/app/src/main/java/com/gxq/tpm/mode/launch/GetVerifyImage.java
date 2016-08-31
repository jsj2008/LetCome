package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.letcome.App;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetWork;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetVerifyImage extends BaseRes {

	private static final long serialVersionUID = -4642823011795264617L;

	
	public static class Params implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2262159725645027121L;
		
		String Accept;
		String width;
		String height;
		String fontSize;
		String UUID;
	}
	
	public static void doRequest(int width, int height, ICallBack netBack) {
//		postParams.put("Accept", "image/*");
//		postParams.put("width", "" + App.instance().dip2px(252 / 2.0f));
//		postParams.put("height", "" + App.instance().dip2px(108 / 2.0f));
//		postParams.put("fontSize", "" + App.instance().sp2px(16f));
//		postParams.put("UUID", UserPrefs.get(LoginActivity.this).getOpenUdid());
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.Accept = NetWork.PARAM_VALUE_IMAGE;
		params.width = Integer.toString(width);
		params.height = Integer.toString(height);
		params.fontSize = Integer.toString(App.instance().sp2px(16f));
		params.UUID = App.getUserPrefs().getOpenUdid();
		
		proxy.getRequest(RequestInfo.GET_VERIFY_IMG, params, ByteArrayRes.class, RETURN_TYPE, false);
	}
	
}
