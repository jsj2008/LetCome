package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ForgetMoblieCheck extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = -6157841886709641651L;
	public String result;//	Y/N
//  79016：手机号码不符合规则
//  79010：手机验证码输入错误
//  79017：手机号码已经注册
    
    public static class Params implements Serializable{
		/**
		 * 
		 */
        private static final long serialVersionUID = 3151149183661297042L;
		public String mobile;//手机号码
        public String code;//手机验证码
        public String UUID;
    }

    public static void doRequest(Params params,ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
////   	HashMap<String, String> postParams = App.instance().postParams();
////		postParams=getHashMapParams(params, postParams, gson);
////		proxy.getRequest(, RequestInfo.LOGIN.getOperationType(), Util.transformString(R.string.service_user), postParams, LoginRequest.class,null,false);
//
//		proxy.postRequest(RequestInfo.FORGET_MOBILE_CHECK.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params),
//		        ForgetMoblieCheck.class, null, false, false);
   		proxy.getRequest(RequestInfo.FORGET_MOBILE_CHECK, params, ForgetMoblieCheck.class, RETURN_TYPE, false);
   	}
   
}
