package com.gxq.tpm.mode.launch;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class Login extends BaseRes {

	/**
	 * 
	 */
    private static final long serialVersionUID = 634810619853574235L;
	
	public long uid; // 用户UID
	
	public String encryptedKey; // 签名加密串
    
    public static class Params implements Serializable{

        private static final long serialVersionUID = 505968956554756499L;
        
        public String login_id; // 手机号/UID/Token
        public String password; // 	密码
        public int login_type; 	// 1-手机的验证码登录、6-UID登陆、7-授权登陆
    }
    
    public static void doRequest(Params params,ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		proxy.getRequest(RequestInfo.LOGIN, params, Login.class, RETURN_TYPE, false);
   	}
   
}
