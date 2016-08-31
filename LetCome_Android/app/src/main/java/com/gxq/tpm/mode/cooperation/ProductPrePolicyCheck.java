package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.ProductPreCheck;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductPrePolicyCheck extends ProductPreCheck {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4571393497160432362L;
	
	public int agreement_type;	

	public static class Params implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4155348292714887431L;
		public int scheme_id; 
		public int investor_id;
		public String stock_code;
		public double fund;
		public int cell_id;

	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.PRODUCT_PRE_POLICY_CHECK, params, ProductPrePolicyCheck.class, RETURN_TYPE, false);
   	}

}
