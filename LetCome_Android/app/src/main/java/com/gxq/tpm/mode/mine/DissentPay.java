package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class DissentPay extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = -949557133701245286L;
    public String result;//结果	string‘Y’成功/‘N’失败
    
    public static class Params implements Serializable {
		/**
		 * 
		 */
        private static final long serialVersionUID = -2061527431266696677L;
        public String p_type;//	产品类型	stock，au，ag，if
        public long p_id;//	产品ID	int
        public long fix_id;//	修正ID	int。
    }
    
    public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//   	HashMap<String, String> postParams = App.instance().postParams();
//		postParams=getHashMapParams(params, postParams, gson);
//		proxy.getRequest(,RequestInfo.S_DISSENT_CORRECTION_PAY.getOperationType(), ServiceConfig.getServicePlatform(), postParams, DissentPay.class,null,false);

//		proxy.postRequest(RequestInfo.S_DISSENT_CORRECTION_PAY.getOperationType(), ServiceConfig.getServicePlatform(),
//		        gson.toJson(params), DissentPay.class, null, false, false);
   		proxy.getRequest(RequestInfo.S_DISSENT_CORRECTION_PAY, params, DissentPay.class, RETURN_TYPE, false);
   	}
}
