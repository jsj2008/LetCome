package com.gxq.tpm.mode;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class SignAgreement extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6358776722880833530L;
	
	public ArrayList<SignResult> signResult;
	public int sign_fail;
	public String result;

	public static class SignResult implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9106418186744158149L;
		public String result;
		public String agreement_id;
		public int error_code;
		public String error_msg;
	}

	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5634523095909365750L;
		
		//public int[] id;
		//public int id;
		public String id;
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.PROTOCOL_SIGN, params, 
				SignAgreement.class, RETURN_TYPE, true);
	}
	
}
