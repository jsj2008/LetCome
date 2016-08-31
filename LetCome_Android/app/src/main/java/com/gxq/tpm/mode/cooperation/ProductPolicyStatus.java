package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductPolicyStatus extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7321206157590636216L;
	public int state; //	该阶段状态 0:处理中 1：成功 2：失败	int
	public String detail; //	失败原因	string

	public static class Params implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4942647304823549076L;
		public int p_id; //	订单id	Int	TRUE
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.PRODUCT_POLICY_STATUS, params, ProductPolicyStatus.class, RETURN_TYPE, false);
   	}

}
