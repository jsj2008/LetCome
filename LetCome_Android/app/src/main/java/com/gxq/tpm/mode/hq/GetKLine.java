package com.gxq.tpm.mode.hq;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetKLine extends BaseRes {

	private static final long serialVersionUID = 5379722498667698498L;
	
	public static final int SINGLE_DAY	= 1;
	public static final int MULTI_DAY	= 2;
	
	public String stockcode;
	public String stockname;
	public String servertime;
	public int Count;
	public ArrayList<KStruct> records;
	
	public static class KStruct implements Serializable {
		private static final long serialVersionUID = -7234515431353994137L;
		
		public int index;
		public float New;
		public float Open;
		public float High;
		public float Low;
		public float YClose;
		public long Volume;
		public long date;
		public String time;
		public float Amount;
	}
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6219374069719378234L;
		
		public String opt;
		public String name;
		public String type;
		public String day_count;
	}
	
	public static void doRequest(String code, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.opt = RequestInfo.GET_K.getOperationType();
		params.name = code;
		if (tag == SINGLE_DAY) {
			params.day_count = "1";
		} else if (tag == MULTI_DAY) {
			params.day_count = "119";
		}
		params.type = GlobalConstant.STOCK_HQ_TYPE;
		proxy.getHQRequest(RequestInfo.GET_K, params, GetKLine.class, tag, true);
	}
	
}
