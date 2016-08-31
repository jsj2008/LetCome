package com.gxq.tpm.mode;

import java.io.Serializable;
import java.util.Map;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class NeedNotice extends BaseRes {

	private static final long serialVersionUID = -3225417997348056042L;
	
	public static String MSG 				= "msg"; 
	public static String ANNO_MSG 			= "anno_msg"; // 公告未读消息数量
	public static String NOTICE_MSG 		= "notice_msg"; // 通知未读消息数量 
	public static String SYSTEM_MSG 		= "system_msg"; // 系统未读消息数量

	public Map<String, Integer> records;
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = -6647670222945904137L;
		
		public String type;
		public long from_time;
	}

	public static void doRequest(Params params, ICallBack netBack) {
		doRequest(params, netBack, RETURN_TYPE);
	}
	
	public static void doRequest(Params params, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.MSG_NEED_NOTICE, params, NeedNotice.class, tag, true);
	}
	
}
