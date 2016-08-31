package com.gxq.tpm.mode.mine;

import java.io.Serializable;
import java.util.List;

import com.gxq.tpm.mode.AgreementBase;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class TradeContractList extends BaseRes {
	private static final long serialVersionUID = 5174338157410987655L;

	public List<Contract> records;
	public int total;
	
	public static class Contract extends AgreementBase {
		private static final long serialVersionUID = 3766345316748589296L;
		
//		public long create_time;
		public long sign_time;
		public int history_id;
	}
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = -5663970379859367441L;
		
//		public int product_type;
		public String start_id;
		public int limit;
		public String p_type;
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//
//		proxy.postRequest(RequestInfo.AGREEMENT_SIGNED_LIST.getOperationType(), ServiceConfig.getServiceUser(), 
//				gson.toJson(params), TradeContractList.class, null, false, true);
		proxy.getRequest(RequestInfo.AGREEMENT_SIGNED_LIST, params,
				TradeContractList.class, RETURN_TYPE, false);
	}
	
}
