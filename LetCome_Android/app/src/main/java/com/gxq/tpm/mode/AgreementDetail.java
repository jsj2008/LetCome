package com.gxq.tpm.mode;

import java.io.Serializable;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class AgreementDetail extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1246668014735480219L;
	
	public String result;//Y: 已签署 N:未已签署
	public String content;	//协议内容
	
	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8257349227352921050L;
		
		public String id;
		
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//
//		proxy.postRequest(RequestInfo.PROTOCOL_CONTENT.getOperationType(), ServiceConfig.getServiceUser(), 
//				gson.toJson(params),InspAgreementDetail.class, null, false, false);
		proxy.getRequest(RequestInfo.PROTOCOL_CONTENT, params,
				AgreementDetail.class, RETURN_TYPE, false);
	}

}
