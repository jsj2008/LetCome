package com.gxq.tpm.mode.hq;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetHQInfo extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6374268523536275313L;
	
	public String servertime;
	public ArrayList<HQInfo> records;
	
	public static int mCurrentTimeType = 0;

	public static class HQInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3508500089976739498L;
		
		public String stockcode;
		public String stockname;
		public float YClose;
		public float New;
		public float Buy1;
		public float Sell1;
	}
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4277802079666833647L;
		
		public String opt;
		public String name;
		public String type;
	}
	
	public static void doRequest(String code, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.opt = RequestInfo.GET_HQ.getOperationType();
		params.name = code;
		params.type = GlobalConstant.STOCK_HQ_TYPE;
		
		proxy.getHQRequest(RequestInfo.GET_HQ, params, GetHQInfo.class, RETURN_TYPE, true);
	}
	
}
