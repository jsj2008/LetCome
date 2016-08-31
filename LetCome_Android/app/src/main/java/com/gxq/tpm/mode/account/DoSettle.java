package com.gxq.tpm.mode.account;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class DoSettle extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1452933040441026218L;
	public String result;

	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8805870688530360025L;
		public int p_id;//	订单id	Int	TRUE
//		public String p_type; //	产品类型	string	TRUE
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.PRODUCT_DO_SETTLE, params, DoSettle.class, RETURN_TYPE, false);
	}
}
