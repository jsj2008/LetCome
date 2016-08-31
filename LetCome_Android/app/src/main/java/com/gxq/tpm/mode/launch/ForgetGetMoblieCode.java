package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ForgetGetMoblieCode extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = -5671896873555318011L;
	public String result;//	Y:发送成功	N:发送失败	String

    
    public static class Params implements Serializable{
		/**
		 * 
		 */
        private static final long serialVersionUID = 6711306094048610390L;
		public String mobile;//
//		public String UUID;
//		public String code;
    }
    
    public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
////   	HashMap<String, String> postParams = App.instance().postParams();
////		postParams=getHashMapParams(params, postParams, gson);
////		proxy.getRequest(, RequestInfo.CHECK_VERIFY.getOperationType(), Util.transformString(R.string.service_user), postParams, GetMoblieCode.class,null,false);
//
//		proxy.postRequest(RequestInfo.FORGET_MOBILE_CODE_GET.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params),
//		        ForgetGetMoblieCode.class, null, false, false);
   		proxy.getRequest(RequestInfo.FORGET_MOBILE_CODE_GET, params, ForgetGetMoblieCode.class, RETURN_TYPE, false);
   	}
  
}
