package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ReadMsg extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3748369941504259448L;
	public String result; //Y：成功 N：失败
	
	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6006156849359895476L;
		public int msg_id;//消息ID
		
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		doRequest(params, netBack, RETURN_TYPE);
	}
	
	public static void doRequest(Params params, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//		HashMap<String, String> postParams = postParams();
//   		postParams = getHashMapParams(params, postParams, gson);
//		
//		proxy.getRequest(RequestInfo.MSG_READ_MSG.getOperationType(), ServiceConfig.getServiceUser(),
//				postParams, InspReadMsg.class, tag, false);
		proxy.getRequest(RequestInfo.MSG_READ_MSG, params, ReadMsg.class, tag, false);
	}
	
}
