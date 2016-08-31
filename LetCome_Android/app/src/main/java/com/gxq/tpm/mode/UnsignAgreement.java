package com.gxq.tpm.mode;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class UnsignAgreement extends BaseRes {

	private static final long serialVersionUID = 6122610605114152974L;

	public String result;
	public ArrayList<Agreement> res_data;
	
	public static class Agreement extends AgreementBase {
		private static final long serialVersionUID = 5495185812710869044L;
		
		public int status;
		public int user_type;
		public String version;
		public long publish_time;
	}

	
	public static class Params implements Serializable {

		private static final long serialVersionUID = -3347966461561988774L;
		
		public int prd_type;//0=平台;1=股票;2=期指;3=黄金;4=白银;7=基金B;8=递延
		public int user_type;//1:策略人 2：投资人 3：风控人
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		doRequest(params, netBack, RETURN_TYPE);
	}
	
	public static void doRequest(Params params, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.PROTOCOL_UNSIGNED, params, UnsignAgreement.class, tag, false);
	}
	
}
