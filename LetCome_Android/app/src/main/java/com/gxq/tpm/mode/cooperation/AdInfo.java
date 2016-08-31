package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class AdInfo extends BaseRes{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2021297357168322275L;
	public ArrayList<Record> records;
	
	public static class Record implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5699267005828976840L;
		public String pic; //图片地址
		public String link; //链接URL
		public long create_time; //时间
		public String title;
		public int iscert; //是否要登陆 1 ：是 0：否
	}
	
	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7118811436882564397L;
		
		public String type; //homepage_ad
		
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		proxy.postRequest(RequestInfo.GET_AD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), InspAdInfo.class, null, false, false);
   		proxy.getRequest(RequestInfo.GET_AD, params, AdInfo.class, RETURN_TYPE, false);
   	}
	
}