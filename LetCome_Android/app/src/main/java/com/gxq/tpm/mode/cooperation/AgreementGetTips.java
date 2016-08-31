package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class AgreementGetTips extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1688648799681326165L;
	public String content;
	
	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3385432986625345041L;
		public int type; //	类型 101：亏损赔付履约金重要提示 102:成本补偿履约金重要提示	Int	TRUE
		public int scheme_id; //	方案ID	int	FALSE
	}

	
	public static void doRequest(Params params,ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.AGREEMENT_GET_TIPS, params, AgreementGetTips.class, RETURN_TYPE, false);
	}
}
