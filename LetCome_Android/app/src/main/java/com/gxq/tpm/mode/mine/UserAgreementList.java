package com.gxq.tpm.mode.mine;

import java.io.Serializable;
import java.util.List;

import com.gxq.tpm.mode.AgreementBase;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class UserAgreementList extends BaseRes {

	private static final long serialVersionUID = -3359519421657734249L;
	
	public List<Agreement> records;
	public int total;
	
	public static class Agreement extends AgreementBase {
		private static final long serialVersionUID = -6321281643453925371L;

		public int status; // 1：已签署 0：未签署
		public long sign_time;
//		public long create_time;
		public long publish_time;
	}
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = -4020357087431544593L;
		
		public int user_type;
		public int product_type;
		public int start_id;
		public int limit;
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//
//		proxy.postRequest(RequestInfo.AGREEMENT_USERAGREEMENT_LIST.getOperationType(),
//				ServiceConfig.getServiceUser(), 
//				gson.toJson(params), UserAgreementList.class, null, false, true);
		proxy.getRequest(RequestInfo.AGREEMENT_USERAGREEMENT_LIST, params,
				UserAgreementList.class, RETURN_TYPE, false);
	}
	
}
