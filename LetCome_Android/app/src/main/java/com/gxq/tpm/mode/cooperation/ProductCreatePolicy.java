package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductCreatePolicy extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1074628602288042371L;
	public int id; //	订单id （成功时返回）	Int
	public String result;
	
	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3164469874002896648L;
		public int scheme_id; //	方案号	Int	TRUE
		public int investor_id; //	投资人ID	Int	TRUE
		public String stock_code; //	股票代码	string	TRUE
		public double fund; //	本金	Float	TRUE
		public int cell_id;
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.PRODUCT_CREATE_POLICY, params, ProductCreatePolicy.class, RETURN_TYPE, false);
   	}

}
