package com.gxq.tpm.mode.hq;

import java.io.Serializable;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetHandicapInfo extends BaseRes {

	private static final long serialVersionUID = 1020883633188930297L;
	
	public String markettime;
	public String servertime;
	public String stockcode;
	public String stockname;
	public float New;
	public float Open;
	public float High;
	public float Low;
	public float YClose;
	public float price_max;
	public float price_min;
	public float Buy1;
	public float Buy2;
	public float Buy3;
	public float Buy4;
	public float Buy5;
	public float Sell1;
	public float Sell2;
	public float Sell3;
	public float Sell4;
	public float Sell5;
	public long BuyVol1;
	public long BuyVol2;
	public long BuyVol3;
	public long BuyVol4;
	public long BuyVol5;
	public long SellVol1;
	public long SellVol2;
	public long SellVol3;
	public long SellVol4;
	public long SellVol5;
//	public int volume;
	public long curvolume;
	public long volume;//for szcz
	public float amount;
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = -100269187842648703L;
		
		String opt;
		String name;
		String type;
		String excode;
	}
	
	public static void doRequest(String code, ICallBack netBack) {
		doRequest(code, netBack, RETURN_TYPE);
	}
	
	public static void doRequest(String code, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.opt = RequestInfo.GET_HANDICAP.getOperationType();
		params.name = code;
		params.type = GlobalConstant.STOCK_HQ_TYPE;
		proxy.getHQRequest(RequestInfo.GET_HANDICAP, params,
				GetHandicapInfo.class, tag, true);
	}
	
	public static void doRequestSH(String code, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		Params params = new Params();
		params.opt = RequestInfo.GET_HANDICAP.getOperationType();
		params.name = code;
		params.type = GlobalConstant.STOCK_HQ_TYPE;
		params.excode = "SH";
		proxy.getHQRequest(RequestInfo.GET_HANDICAP, params,
				GetHandicapInfo.class, RETURN_TYPE, true);
	}
	
}
