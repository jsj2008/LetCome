package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class AccountInfo extends BaseRes{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5729124153827584639L;
	public String amount_sp; //实盘券账户
	public String amount_jf; //积分账户
	public getamount getamount;
	public static class getamount{
		public AmountInfo jf;
		public AmountInfo sp;
	}
	
	public static class AmountInfo implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8616744087110611627L;
		public String available;
		public String freeze;
	}
	
    public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2599047972070303232L;
		public long uid;
    }
    
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//
//		proxy.postRequest(RequestInfo.MYACCOUNT_MOUNT_URL.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), AccountInfo.class,
//		        null, false, true);
		proxy.getRequest(RequestInfo.MYACCOUNT_MOUNT_URL, params, AccountInfo.class, RETURN_TYPE, false);
	}

}
