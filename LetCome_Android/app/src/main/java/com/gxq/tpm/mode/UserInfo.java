package com.gxq.tpm.mode;

import java.io.Serializable;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class UserInfo extends BaseRes {
    /**
	 * 
	 */
    private static final long serialVersionUID = 677136282358056692L;
	public String uid;//	　	Int
    public String nick_name;//	昵称	String
    public int user_type;//	用户类型 1:策略人 2：投资人 3：风控人	Int
    public String bind_mobile;//	绑定手机号:前3位+后4位    138****0909	String
    public String bind_mail;//	绑定邮箱	String
    public String bind_weixin;//	绑定微信	String
    public String bind_prod;//	绑定操盘宝	String
    public String pic;//	头像地址	String
    public long create_time;//   账号创建时间
    public String prod_username; //绑定操盘宝用户姓名
    public int bind_cpb_tblid;//用于判断是否绑定操盘宝
    public int nicknm_status; // 2重置
	
    public static class Params implements Serializable{
    	/**
		 * 
		 */
        private static final long serialVersionUID = -949892755322143169L;
		public long uid;
    }
    
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.USER_INFO, params, UserInfo.class, RETURN_TYPE, true);
	}
	
	public static void doRequestByTag(Params params, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.USER_INFO, params, UserInfo.class, tag, false);
	}
	
	public static void doRequest(Params params, ICallBack netBack, int outTime) {
		NetworkProxy proxy = new NetworkProxy(netBack, outTime);

		proxy.getRequest(RequestInfo.USER_INFO, params, UserInfo.class, RETURN_TYPE, false);
	}
}
