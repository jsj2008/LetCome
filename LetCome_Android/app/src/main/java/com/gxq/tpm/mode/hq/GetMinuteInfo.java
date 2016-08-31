package com.gxq.tpm.mode.hq;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetMinuteInfo extends BaseRes {

	private static final long serialVersionUID = -6212889588425699012L;

	public String stockcode;
	public String stockname;
	public float Yclose;
	public float New;
	public float Open;
	public float High;
	public float Low;
	public String markettime;
	public String servertime;
	public int Count;
//	public float[] records;
	public ArrayList<float[]> records;
	
	public static final int NEW = 0;
	public static final int VOLUME = 1;
	
	public static class Params implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4381942857850196456L;
		String opt;
		String name;
		String short_term = "1";
		String type;
	}
	
	public static void doRequest(String code, ICallBack netBack) {
		doRequest(code, netBack, RETURN_TYPE);
	}
	
	public static void doRequest(String code, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		Params params = new Params();
		params.opt = RequestInfo.GET_MINUTE.getOperationType();
		params.name = code;
		params.type = GlobalConstant.STOCK_HQ_TYPE;

		proxy.getHQRequest(RequestInfo.GET_MINUTE, params, GetMinuteInfo.class, tag, true);
	}
	
}
